package com.juul.indexeddb

import com.juul.indexeddb.external.IDBTransaction
import kotlinext.js.jsObject

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

    public suspend fun Queryable.getAll(key: Key? = null): Array<dynamic> {
        val request = requestGetAll(key).request
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
        Index(objectStore.createIndex(name, keyPath.toUnwrappedJs(), jsObject { this.unique = unique }))
}
