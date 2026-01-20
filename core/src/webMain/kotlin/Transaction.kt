package com.juul.indexeddb

import com.juul.indexeddb.external.Event
import com.juul.indexeddb.external.IDBCursor
import com.juul.indexeddb.external.IDBKey
import com.juul.indexeddb.external.IDBRequest
import com.juul.indexeddb.external.IDBTransaction
import com.juul.indexeddb.external.IDBValue
import com.juul.indexeddb.logs.Logger
import com.juul.indexeddb.logs.NoOpLogger
import com.juul.indexeddb.logs.Type
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlin.js.JsAny
import kotlin.js.JsArray
import kotlin.js.toInt

public open class Transaction internal constructor(
    internal val transaction: IDBTransaction,
    internal val logger: Logger,
    internal val transactionId: Long,
) {
    internal var operationId: Int = 0

    internal suspend fun awaitCompletion(onComplete: ((Event) -> Unit)? = null) {
        transaction.onNextEvent("complete", "abort", "error") { event ->
            onComplete?.invoke(event)
            when (event.type) {
                "abort" -> throw AbortTransactionException(event)
                "error" -> throw ErrorEventException(event)
                else -> Unit
            }
        }
    }

    internal suspend fun awaitFailure() {
        transaction.onNextEvent("complete", "abort", "error") { event ->
            when (event.type) {
                "abort" -> Unit
                "error" -> Unit
                else -> Unit
            }
        }
    }

    internal fun abort() {
        logger.log(Type.Transaction) { "Aborting transaction" }
        transaction.abort()
    }

    internal fun commit() {
        transaction.commit()
    }

    internal suspend inline fun <T : JsAny?> Queryable.request(
        functionName: String,
        crossinline makeRequest: () -> IDBRequest<T>,
    ): T {
        val id = operationId++
        logger.log(Type.Query) { "$functionName request on $type `$name` (transaction $transactionId, operation $id)" }
        val request = makeRequest()
        return request.onNextEvent("success", "error") { event ->
            logger.log(Type.Query, event) {
                "$functionName response on $type `$name` (transaction $transactionId, operation $id)"
            }
            when (event.type) {
                "error" -> throw ErrorEventException(event)
                else -> request.result
            }
        }
    }

    public fun objectStore(name: String): ObjectStore =
        ObjectStore(transaction.objectStore(name))

    public suspend fun Queryable.get(key: Key): IDBValue =
        request("get") { requestGet(key).request }

    public suspend fun Queryable.getAll(query: Key? = null, count: UInt? = null): JsArray<IDBValue> =
        request("getAll") { requestGetAll(query, count).request }

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
        "openCursor",
        query,
        direction,
        cursorStart,
        open = this::requestOpenCursor,
        wrap = ::CursorWithValue,
        autoContinue,
        logger,
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
        "openKeyCursor",
        query,
        direction,
        cursorStart,
        open = this::requestOpenKeyCursor,
        wrap = ::Cursor,
        autoContinue,
        logger,
    )

    /**
     * Opens a key cursor, then immediately close it. This has the effect of being the minimally expensive query that
     * still waits for the transaction to be available.
     */
    internal suspend fun Queryable.awaitTransaction() {
        openCursorImpl(
            "openKeyCursor",
            query = null,
            direction = Cursor.Direction.Next,
            cursorStart = null,
            open = this::requestOpenKeyCursor,
            wrap = ::Cursor,
            autoContinue = false,
            logger = NoOpLogger,
        ).collect {
            it.close()
        }
        // Since this function is an internal implementation detail, undo incrementing the operation id to avoid
        // confusion where request 0 went.
        operationId -= 1
    }

    private fun <T : Cursor, U : IDBCursor> Queryable.openCursorImpl(
        functionName: String,
        query: Key?,
        direction: Cursor.Direction,
        cursorStart: CursorStart?,
        open: (Key?, Cursor.Direction) -> Request<U?>,
        wrap: (U, SendChannel<*>) -> T,
        autoContinue: Boolean,
        logger: Logger,
    ): Flow<T> = callbackFlow {
        val id = operationId++
        logger.log(Type.Cursor) { "$functionName request on $type `$name` (transaction $transactionId, operation $id)" }

        var cursorStartAction = cursorStart
        val request = open(query, direction).request
        var finished = false
        val onSuccess: (Event) -> Unit = { event ->
            @Suppress("UNCHECKED_CAST")
            val cursor = (event.target as IDBRequest<U?>).result
            if (cursorStartAction != null && cursor != null) {
                cursorStartAction?.apply(cursor)
                cursorStartAction = null
            } else if (cursor != null) {
                logger.log(Type.Cursor, event) {
                    "Cursor value on $type `$name` (transaction $transactionId, operation $id)"
                }
                val result = trySend(wrap(cursor, channel))
                when {
                    result.isSuccess -> if (autoContinue && !finished) cursor.`continue`()
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
            logger.log(Type.Cursor) { "Cursor closed on $type `$name` (transaction $transactionId, operation $id)" }
            finished = true
            request.removeEventListener("success", onSuccess)
            request.removeEventListener("error", onError)
        }
    }

    public suspend fun Queryable.count(query: Key? = null): Int =
        request("count") { requestCount(query).request }.toInt()

    public fun ObjectStore.index(name: String): Index =
        Index(objectStore.index(name))
}

public open class WriteTransaction internal constructor(
    transaction: IDBTransaction,
    logger: Logger,
    transactionId: Long,
) : Transaction(transaction, logger, transactionId) {

    /**
     * Adds a new item to the database using an in-line or auto-incrementing key. If an item with the same
     * key already exists, this will fail.
     *
     * This API is delicate. If you're passing in Kotlin objects directly, you're probably doing it wrong.
     *
     * Generally, you'll want to create an explicit `external interface` and pass that in, to guarantee that Kotlin
     * doesn't mangle, prefix, or otherwise mess with your field names.
     */
    public suspend fun ObjectStore.add(item: IDBValue): IDBKey =
        request("add") { objectStore.add(item) }

    /**
     * Adds a new item to the database using an explicit out-of-line key. If an item with the same key already
     * exists, this will fail.
     *
     * This API is delicate. If you're passing in Kotlin objects directly, you're probably doing it wrong.
     *
     * Generally, you'll want to create an explicit `external interface` and pass that in, to guarantee that Kotlin
     * doesn't mangle, prefix, or otherwise mess with your field names.
     */
    public suspend fun ObjectStore.add(item: IDBValue, key: Key): IDBKey =
        request("add") { objectStore.add(item, key.toJs()) }

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
    public suspend fun ObjectStore.put(item: IDBValue): IDBKey =
        request("put") { objectStore.put(item) }

    /**
     * Adds an item to or updates an item in the database using an explicit out-of-line key. If an item with the
     * same key already exists, this will replace that item.
     *
     * This API is delicate. If you're passing in Kotlin objects directly, you're probably doing it wrong.
     *
     * Generally, you'll want to create an explicit `external interface` and pass that in, to guarantee that Kotlin
     * doesn't mangle, prefix, or otherwise mess with your field names.
     */
    public suspend fun ObjectStore.put(item: IDBValue, key: Key): IDBKey =
        request("put") { objectStore.put(item, key.toJs()) }

    public suspend fun ObjectStore.delete(key: Key) {
        request("delete") { objectStore.delete(key.toJs()) }
    }

    public suspend fun ObjectStore.clear() {
        request("clear") { objectStore.clear() }
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

    public suspend fun CursorWithValue.update(value: JsAny) {
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
    logger: Logger,
    transactionId: Long,
) : WriteTransaction(transaction, logger, transactionId) {

    /** Creates an object-store that uses explicit out-of-line keys. */
    public fun Database.createObjectStore(name: String): ObjectStore {
        logger.log(Type.Database) { "Creating object store: $name" }
        return ObjectStore(ensureDatabase().createObjectStore(name))
    }

    /** Creates an object-store that uses in-line keys. */
    public fun Database.createObjectStore(name: String, keyPath: KeyPath): ObjectStore {
        logger.log(Type.Database) { "Creating object store: $name" }
        return ObjectStore(ensureDatabase().createObjectStore(name, keyPath.toOptions()))
    }

    /** Creates an object-store that uses out-of-line keys with a key-generator. */
    public fun Database.createObjectStore(name: String, autoIncrement: AutoIncrement): ObjectStore {
        logger.log(Type.Database) { "Creating object store: $name" }
        return ObjectStore(ensureDatabase().createObjectStore(name, autoIncrement.toOptions()))
    }

    public fun Database.deleteObjectStore(name: String) {
        logger.log(Type.Database) { "Deleting object store: $name" }
        ensureDatabase().deleteObjectStore(name)
    }

    public fun ObjectStore.createIndex(name: String, keyPath: KeyPath, unique: Boolean): Index {
        logger.log(Type.Database) { "Creating index: $name" }
        return Index(objectStore.createIndex(name, keyPath.toJs(), jso { this.unique = unique }))
    }

    public fun ObjectStore.deleteIndex(name: String) {
        logger.log(Type.Database) { "Deleting index: $name" }
        objectStore.deleteIndex(name)
    }
}
