package com.juul.indexeddb

import com.juul.indexeddb.external.IDBCursor
import com.juul.indexeddb.external.IDBRequest
import com.juul.indexeddb.external.IDBTransaction
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import org.w3c.dom.events.Event

public open class Transaction internal constructor(
    internal val transaction: IDBTransaction,
) {

    internal suspend fun awaitCompletion() {
        transaction.onNextEvent("complete", "abort", "error") { event ->
            when (event.type) {
                "abort" -> throw AbortTransactionException(event)
                "error" -> throw ErrorEventException(event)
                else -> Unit
            }
        }
    }

    public fun objectStore(name: String): ObjectStore =
        ObjectStore(transaction.objectStore(name))

    public suspend fun Queryable.get(key: Key): dynamic {
        val request = requestGet(key).request
        return request.onNextEvent("success", "error") { event ->
            when (event.type) {
                "error" -> throw ErrorEventException(event)
                else -> request.result
            }
        }
    }

    public suspend fun Queryable.getAll(query: Key? = null): Array<dynamic> {
        val request = requestGetAll(query).request
        return request.onNextEvent("success", "error") { event ->
            when (event.type) {
                "error" -> throw ErrorEventException(event)
                else -> request.result
            }
        }
    }

    @Deprecated(
        "In the future, `autoContinue` will be a required parameter.",
        ReplaceWith("openCursor(query, direction, cursorStart, autoContinue = true)"),
    )
    public suspend fun Queryable.openCursor(
        query: Key? = null,
        direction: Cursor.Direction = Cursor.Direction.Next,
        cursorStart: CursorStart? = null,
    ): Flow<CursorWithValue> = openCursor(
        query,
        direction,
        cursorStart,
        autoContinue = true,
    )

    /**
     * When [autoContinue] is `true`, all values matching the query will emit automatically. It is important
     * not to suspend in flow collection on _anything_ other than flow operators (such as `.toList`).
     *
     * Warning: when [autoContinue] is `false`, callers are responsible for data flow and must call `continue`, `advance`,
     * or similar explicitly. The returned flow will terminate automatically after `continue` if no more elements
     * remain. Otherwise, you must call `close` to terminate the flow. Failing to call `continue` or `close`
     * will result in the flow stalling.
     */
    public suspend fun Queryable.openCursor(
        query: Key? = null,
        direction: Cursor.Direction = Cursor.Direction.Next,
        cursorStart: CursorStart? = null,
        autoContinue: Boolean,
    ): Flow<CursorWithValue> = openCursorImpl(
        query,
        direction,
        cursorStart,
        open = this::requestOpenCursor,
        wrap = ::CursorWithValue,
        autoContinue,
    )

    @Deprecated(
        "In the future, `autoContinue` will be a required parameter.",
        ReplaceWith("openKeyCursor(query, direction, cursorStart, autoContinue = true)"),
    )
    public suspend fun Queryable.openKeyCursor(
        query: Key? = null,
        direction: Cursor.Direction = Cursor.Direction.Next,
        cursorStart: CursorStart? = null,
    ): Flow<Cursor> = openKeyCursor(
        query,
        direction,
        cursorStart,
        autoContinue = true,
    )

    /**
     * When [autoContinue] is `true`, all values matching the query will emit automatically. It is important
     * not to suspend in flow collection on _anything_ other than flow operators (such as `.toList`).
     *
     * Warning: when [autoContinue] is `false`, callers are responsible for data flow and must call `continue`, `advance`,
     * or similar explicitly. The returned flow will terminate automatically after `continue` if no more elements
     * remain. Otherwise, you must call `close` to terminate the flow. Failing to call `continue` or `close`
     * will result in the flow stalling.
     */
    public suspend fun Queryable.openKeyCursor(
        query: Key? = null,
        direction: Cursor.Direction = Cursor.Direction.Next,
        cursorStart: CursorStart? = null,
        autoContinue: Boolean,
    ): Flow<Cursor> = openCursorImpl(
        query,
        direction,
        cursorStart,
        open = this::requestOpenKeyCursor,
        wrap = ::Cursor,
        autoContinue,
    )

    private suspend fun <T : Cursor, U : IDBCursor> openCursorImpl(
        query: Key?,
        direction: Cursor.Direction,
        cursorStart: CursorStart?,
        open: (Key?, Cursor.Direction) -> Request<U?>,
        wrap: (U, SendChannel<*>) -> T,
        autoContinue: Boolean,
    ): Flow<T> = callbackFlow {
        var cursorStartAction = cursorStart
        val request = open(query, direction).request
        val onSuccess: (Event) -> Unit = { event ->
            @Suppress("UNCHECKED_CAST")
            val cursor = (event.target as IDBRequest<U?>).result
            if (cursorStartAction != null && cursor != null) {
                cursorStartAction?.apply(cursor)
                cursorStartAction = null
            } else if (cursor != null) {
                val result = trySend(wrap(cursor, channel))
                when {
                    result.isSuccess -> if (autoContinue) cursor.`continue`()
                    result.isFailure -> channel.close(IllegalStateException("Send failed. Did you suspend illegally?"))
                    result.isClosed -> channel.close()
                }
            } else {
                channel.close()
            }
        }
        val onError: (Event) -> Unit = { event -> channel.close(ErrorEventException(event)) }
        request.addEventListener("success", onSuccess)
        request.addEventListener("error", onError)
        awaitClose {
            request.removeEventListener("success", onSuccess)
            request.removeEventListener("error", onError)
        }
    }

    public suspend fun Queryable.count(query: Key? = null): Int {
        val request = requestCount(query).request
        return request.onNextEvent("success", "error") { event ->
            when (event.type) {
                "error" -> throw ErrorEventException(event)
                else -> request.result
            }
        }
    }

    public fun ObjectStore.index(name: String): Index =
        Index(objectStore.index(name))
}

public open class WriteTransaction internal constructor(
    transaction: IDBTransaction,
) : Transaction(transaction) {

    /**
     * Adds a new item to the database using an in-line or auto-incrementing key. If an item with the same
     * key already exists, this will fail.
     *
     * This API is delicate. If you're passing in Kotlin objects directly, you're probably doing it wrong.
     *
     * Generally, you'll want to create an explicit `external interface` and pass that in, to guarantee that Kotlin
     * doesn't mangle, prefix, or otherwise mess with your field names.
     */
    public suspend fun ObjectStore.add(item: dynamic): dynamic {
        val request = objectStore.add(item)
        return request.onNextEvent("success", "error") { event ->
            when (event.type) {
                "error" -> throw ErrorEventException(event)
                else -> request.result
            }
        }
    }

    /**
     * Adds a new item to the database using an explicit out-of-line key. If an item with the same key already
     * exists, this will fail.
     *
     * This API is delicate. If you're passing in Kotlin objects directly, you're probably doing it wrong.
     *
     * Generally, you'll want to create an explicit `external interface` and pass that in, to guarantee that Kotlin
     * doesn't mangle, prefix, or otherwise mess with your field names.
     */
    public suspend fun ObjectStore.add(item: dynamic, key: Key): dynamic {
        val request = objectStore.add(item, key.toJs())
        return request.onNextEvent("success", "error") { event ->
            when (event.type) {
                "error" -> throw ErrorEventException(event)
                else -> request.result
            }
        }
    }

    /**
     * Adds an item to or updates an item in the database using an in-line or auto-incrementing key. If an item
     * with the same key already exists, this will replace that item. Note that with auto-incrementing keys a new
     * item will always be inserted.
     *
     * This API is delicate. If you're passing in Kotlin objects directly, you're probably doing it wrong.
     *
     * Generally, you'll want to create an explicit `external interface` and pass that in, to guarantee that Kotlin
     * doesn't mangle, prefix, or otherwise mess with your field names.
     */
    public suspend fun ObjectStore.put(item: dynamic): dynamic {
        val request = objectStore.put(item)
        return request.onNextEvent("success", "error") { event ->
            when (event.type) {
                "error" -> throw ErrorEventException(event)
                else -> request.result
            }
        }
    }

    /**
     * Adds an item to or updates an item in the database using an explicit out-of-line key. If an item with the
     * same key already exists, this will replace that item.
     *
     * This API is delicate. If you're passing in Kotlin objects directly, you're probably doing it wrong.
     *
     * Generally, you'll want to create an explicit `external interface` and pass that in, to guarantee that Kotlin
     * doesn't mangle, prefix, or otherwise mess with your field names.
     */
    public suspend fun ObjectStore.put(item: dynamic, key: Key): dynamic {
        val request = objectStore.put(item, key.toJs())
        return request.onNextEvent("success", "error") { event ->
            when (event.type) {
                "error" -> throw ErrorEventException(event)
                else -> request.result
            }
        }
    }

    public suspend fun ObjectStore.delete(key: Key) {
        val request = objectStore.delete(key.toJs())
        request.onNextEvent("success", "error") { event ->
            when (event.type) {
                "error" -> throw ErrorEventException(event)
                else -> Unit
            }
        }
    }

    public suspend fun ObjectStore.clear() {
        val request = objectStore.clear()
        request.onNextEvent("success", "error") { event ->
            when (event.type) {
                "error" -> throw ErrorEventException(event)
                else -> Unit
            }
        }
    }

    public suspend fun CursorWithValue.delete() {
        val request = cursor.delete()
        request.onNextEvent("success", "error") { event ->
            when (event.type) {
                "error" -> throw ErrorEventException(event)
                else -> Unit
            }
        }
    }

    public suspend fun CursorWithValue.update(value: dynamic) {
        val request = cursor.update(value)
        request.onNextEvent("success", "error") { event ->
            when (event.type) {
                "error" -> throw ErrorEventException(event)
                else -> Unit
            }
        }
    }
}

public class VersionChangeTransaction internal constructor(
    transaction: IDBTransaction,
) : WriteTransaction(transaction) {

    /** Creates an object-store that uses explicit out-of-line keys. */
    public fun Database.createObjectStore(name: String): ObjectStore =
        ObjectStore(database.createObjectStore(name))

    /** Creates an object-store that uses in-line keys. */
    public fun Database.createObjectStore(name: String, keyPath: KeyPath): ObjectStore =
        ObjectStore(database.createObjectStore(name, keyPath.toWrappedJs()))

    /** Creates an object-store that uses out-of-line keys with a key-generator. */
    public fun Database.createObjectStore(name: String, autoIncrement: AutoIncrement): ObjectStore =
        ObjectStore(database.createObjectStore(name, autoIncrement.toJs()))

    public fun Database.deleteObjectStore(name: String) {
        database.deleteObjectStore(name)
    }

    public fun ObjectStore.createIndex(name: String, keyPath: KeyPath, unique: Boolean): Index =
        Index(objectStore.createIndex(name, keyPath.toUnwrappedJs(), jso { this.unique = unique }))
}
