package com.juul.indexeddb

import com.juul.indexeddb.external.IDBDatabase
import com.juul.indexeddb.external.IDBFactory
import com.juul.indexeddb.external.IDBVersionChangeEvent
import com.juul.indexeddb.external.indexedDB
import com.juul.indexeddb.logs.Logger
import com.juul.indexeddb.logs.NoOpLogger
import com.juul.indexeddb.logs.Type
import kotlinx.browser.window
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.w3c.dom.events.Event

/**
 * Inside the [initialize] block, you must not call any `suspend` functions except for:
 * - those provided by this library and scoped on [Transaction] (and its subclasses)
 * - flow operations on the flows returns by [Transaction.openCursor] and [Transaction.openKeyCursor]
 * - `suspend` functions composed entirely of other legal functions
 */
public suspend fun openDatabase(
    name: String,
    version: Int,
    logger: Logger = NoOpLogger,
    initialize: suspend VersionChangeTransaction.(
        database: Database,
        oldVersion: Int,
        newVersion: Int,
    ) -> Unit,
): Database = withContext(Dispatchers.Unconfined) {
    val indexedDB: IDBFactory? = js("self.indexedDB || self.webkitIndexedDB") as? IDBFactory
    val factory = checkNotNull(indexedDB) { "Your browser doesn't support IndexedDB." }
    logger.log(Type.DatabaseOpen) { "Opening database `$name` at version `$version`" }
    val request = factory.open(name, version)
    val versionChangeEvent = request.onNextEvent("success", "upgradeneeded", "error", "blocked") { event ->
        when (event.type) {
            "upgradeneeded" -> event as IDBVersionChangeEvent
            "error" -> throw ErrorEventException(event)
            "blocked" -> throw OpenBlockedException(name, event)
            else -> null
        }
    }
    Database(request.result, logger).also { database ->
        if (versionChangeEvent != null) {
            logger.log(Type.DatabaseUpgrade, versionChangeEvent) {
                "Upgrading database `$name` from version `${versionChangeEvent.oldVersion}` to `${versionChangeEvent.newVersion}`"
            }
            val transaction = VersionChangeTransaction(checkNotNull(request.transaction))
            transaction.initialize(database, versionChangeEvent.oldVersion, versionChangeEvent.newVersion)
            transaction.awaitCompletion()
        }
        logger.log(Type.DatabaseOpen) { "Opened database `$name`" }
    }
}

public suspend fun deleteDatabase(name: String) {
    val factory = checkNotNull(window.indexedDB) { "Your browser doesn't support IndexedDB." }
    val request = factory.deleteDatabase(name)
    request.onNextEvent("success", "error", "blocked") { event ->
        when (event.type) {
            "error", "blocked" -> throw ErrorEventException(event)
            else -> null
        }
    }
}

public class Database internal constructor(
    database: IDBDatabase,
    private val logger: Logger,
) {
    private val name = database.name
    private var database: IDBDatabase? = database

    init {
        val callback = { event: Event ->
            logger.log(Type.DatabaseClose, event) { "Closing database `$name` due to event" }
            onClose()
        }
        // listen for database structure changes (e.g., upgradeneeded while DB is open or deleteDatabase)
        database.addEventListener("versionchange", callback)
        // listen for force close, e.g., browser profile on a USB drive that's ejected or db deleted through dev tools
        database.addEventListener("close", callback)
    }

    internal fun ensureDatabase(): IDBDatabase = checkNotNull(database) { "database is closed" }

    /**
     * Inside the [action] block, you must not call any `suspend` functions except for:
     * - those provided by this library and scoped on [Transaction] (and its subclasses)
     * - flow operations on the flows returns by [Transaction.openCursor] and [Transaction.openKeyCursor]
     * - `suspend` functions composed entirely of other legal functions
     */
    public suspend fun <T> transaction(
        vararg store: String,
        durability: Durability = Durability.Default,
        action: suspend Transaction.() -> T,
    ): T = withContext(Dispatchers.Unconfined) {
        val transaction = Transaction(
            ensureDatabase().transaction(arrayOf(*store), "readonly", transactionOptions(durability)),
        )
        val result = transaction.action()
        transaction.awaitCompletion()
        result
    }

    /**
     * Inside the [action] block, you must not call any `suspend` functions except for:
     * - those provided by this library and scoped on [Transaction] (and its subclasses)
     * - flow operations on the flows returns by [Transaction.openCursor] and [Transaction.openKeyCursor]
     * - `suspend` functions composed entirely of other legal functions
     */
    public suspend fun <T> writeTransaction(
        vararg store: String,
        durability: Durability = Durability.Default,
        action: suspend WriteTransaction.() -> T,
    ): T = withContext(Dispatchers.Unconfined) {
        val transaction = WriteTransaction(
            ensureDatabase().transaction(arrayOf(*store), "readwrite", transactionOptions(durability)),
        )
        with(transaction) {
            // Force overlapping transactions to not call `action` until prior transactions complete.
            objectStore(store.first())
                .openKeyCursor(autoContinue = false)
                .collect { it.close() }
        }
        val result = transaction.action()
        transaction.awaitCompletion()
        result
    }

    public fun close() {
        logger.log(Type.DatabaseClose) { "Closing database `$name` due to explicit `close()`" }
        onClose()
    }

    private fun onClose() {
        val db = database
        if (db != null) {
            db.close()
            database = null
            logger.log(Type.DatabaseClose) { "Closed database `$name`" }
        } else {
            logger.log(Type.DatabaseClose) { "Close skipped, database `$name` already closed" }
        }
    }
}

private fun transactionOptions(durability: Durability): dynamic = jso {
    this.durability = durability.jsValue
}
