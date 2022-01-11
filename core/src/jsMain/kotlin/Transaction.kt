package com.juul.indexeddb

import com.juul.indexeddb.external.IDBCursor
import com.juul.indexeddb.external.IDBRequest
import com.juul.indexeddb.external.IDBTransaction
import kotlinext.js.jso
import kotlinx.coroutines.ExperimentalCoroutinesApi
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

    public suspend fun Queryable.openCursor(
        query: Key? = null,
        direction: Cursor.Direction = Cursor.Direction.Next,
    ): Flow<CursorWithValue> = openCursorImpl(
        query,
        direction,
        open = this::requestOpenCursor,
        wrap = ::CursorWithValue
    )

    public suspend fun Queryable.openKeyCursor(
        query: Key? = null,
        direction: Cursor.Direction = Cursor.Direction.Next,
    ): Flow<Cursor> = openCursorImpl(
        query,
        direction,
        open = this::requestOpenKeyCursor,
        wrap = ::Cursor
    )

    @OptIn(ExperimentalCoroutinesApi::class)
    private suspend fun <T : Cursor, U : IDBCursor> openCursorImpl(
        query: Key?,
        direction: Cursor.Direction,
        open: (Key?, Cursor.Direction) -> Request<U?>,
        wrap: (U) -> T,
    ): Flow<T> = callbackFlow {
        val request = open(query, direction).request
        val onSuccess: (Event) -> Unit = { event ->
            @Suppress("UNCHECKED_CAST")
            val cursor = (event.target as IDBRequest<U?>).result
            if (cursor != null) {
                val result = trySend(wrap(cursor))
                when {
                    result.isSuccess -> cursor.`continue`()
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

    public fun ObjectStore.index(name: String): Index =
        Index(objectStore.index(name))
}

public open class WriteTransaction internal constructor(
    transaction: IDBTransaction,
) : Transaction(transaction) {

    /**
     * Adds a new item to the database. If an item with the same key already exists, this will fail.
     *
     * This API is delicate. If you're passing in Kotlin objects directly, you're probably doing it wrong.
     *
     * Generally, you'll want to create an explicit `external interface` and pass that in, to guarantee that Kotlin
     * doesn't mangle, prefix, or otherwise mess with your field names.
     */
    public suspend fun ObjectStore.add(item: dynamic) {
        val request = objectStore.add(item)
        request.onNextEvent("success", "error") { event ->
            when (event.type) {
                "error" -> throw ErrorEventException(event)
                else -> Unit
            }
        }
    }

    /**
     * Adds an item to, or updates an item in, the database. If an item with the same key already exists, this will replace that item.
     *
     * This API is delicate. If you're passing in Kotlin objects directly, you're probably doing it wrong.
     *
     * Generally, you'll want to create an explicit `external interface` and pass that in, to guarantee that Kotlin
     * doesn't mangle, prefix, or otherwise mess with your field names.
     */
    public suspend fun ObjectStore.put(item: dynamic) {
        val request = objectStore.put(item)
        request.onNextEvent("success", "error") { event ->
            when (event.type) {
                "error" -> throw ErrorEventException(event)
                else -> Unit
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
    public fun Database.createObjectStore(name: String, keyPath: KeyPath): ObjectStore =
        ObjectStore(database.createObjectStore(name, keyPath.toWrappedJs()))

    public fun Database.createObjectStore(name: String, autoIncrement: AutoIncrement): ObjectStore =
        ObjectStore(database.createObjectStore(name, autoIncrement.toJs()))

    public fun Database.deleteObjectStore(name: String) {
        database.deleteObjectStore(name)
    }

    public fun ObjectStore.createIndex(name: String, keyPath: KeyPath, unique: Boolean): Index =
        Index(objectStore.createIndex(name, keyPath.toUnwrappedJs(), jso { this.unique = unique }))
}
