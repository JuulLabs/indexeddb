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
    logger.log(Type.Database) { "Opening database `$name` at version `$version`" }
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
            logger.log(Type.Database, versionChangeEvent) {
                "Upgrading database `$name` from version `${versionChangeEvent.oldVersion}` to `${versionChangeEvent.newVersion}`"
            }
            val id = database.transactionId++
            logger.log(Type.Transaction) { "Opening versionchange transaction $id on database `$name`" }
            val transaction = VersionChangeTransaction(checkNotNull(request.transaction), logger, id)
            transaction.initialize(database, versionChangeEvent.oldVersion, versionChangeEvent.newVersion)
            transaction.awaitCompletion { event ->
                logger.log(Type.Transaction, event) { "Closed versionchange transaction $id on database `$name`" }
            }
        }
        logger.log(Type.Database) { "Opened database `$name`" }
    }
}

public suspend fun deleteDatabase(
    name: String,
    logger: Logger = NoOpLogger,
) {
    logger.log(Type.Database) { "Deleting database `$name`" }
    val factory = checkNotNull(window.indexedDB) { "Your browser doesn't support IndexedDB." }
    val request = factory.deleteDatabase(name)
    request.onNextEvent("success", "error", "blocked") { event ->
        when (event.type) {
            "error", "blocked" -> {
                logger.log(Type.Database, event) { "Delete failed for database `$name`" }
                throw ErrorEventException(event)
            }

            else -> logger.log(Type.Database, event) { "Deleted database `$name`" }
        }
    }
}

public class Database internal constructor(
    database: IDBDatabase,
    private val logger: Logger,
) {
    private val name = database.name
    private var database: IDBDatabase? = database
    internal var transactionId = 0L

    init {
        val callback = { event: Event ->
            logger.log(Type.Database, event) { "Closing database `$name` due to event" }
            tryClose()
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
        val id = transactionId++
        logger.log(Type.Transaction) {
            "Opened readonly transaction $id using stores ${store.joinToString { "`$it`" }} on database `$name`"
        }

        val transaction = Transaction(
            ensureDatabase().transaction(arrayOf(*store), "readonly", transactionOptions(durability)),
            logger,
            id,
        )
        val result = transaction.action()
        transaction.commit()
        transaction.awaitCompletion { event ->
            logger.log(Type.Transaction, event) { "Closed readonly transaction $id on database `$name`" }
        }
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
        val id = transactionId++
        logger.log(Type.Transaction) {
            "Opening readwrite transaction $id using stores ${store.joinToString { "`$it`" }} on database `$name`"
        }

        val transaction = WriteTransaction(
            ensureDatabase().transaction(arrayOf(*store), "readwrite", transactionOptions(durability)),
            logger,
            id,
        )
        with(transaction) {
            // Force overlapping transactions to not call `action` until prior transactions complete.
            objectStore(store.first()).awaitTransaction()
        }
        val result = try {
            transaction.action()
        } catch (e: Throwable) {
            transaction.abort()
            transaction.awaitFailure()
            throw e
        }
        transaction.commit()
        transaction.awaitCompletion { event ->
            logger.log(Type.Transaction, event) { "Closed readwrite transaction $id on database `$name`" }
        }
        result
    }

    public fun close() {
        logger.log(Type.Database) { "Closing database `$name` due to explicit `close()`" }
        tryClose()
    }

    private fun tryClose() {
        val db = database
        if (db != null) {
            db.close()
            database = null
            logger.log(Type.Database) { "Closed database `$name`" }
        } else {
            logger.log(Type.Database) { "Close skipped, database `$name` already closed" }
        }
    }
}

private fun transactionOptions(durability: Durability): dynamic = jso {
    this.durability = durability.jsValue
}
