package com.juul.indexeddb

import com.juul.indexeddb.external.IDBCursor
import com.juul.indexeddb.external.IDBCursorWithValue
import com.juul.indexeddb.external.IDBKey
import com.juul.indexeddb.external.IDBValue
import kotlinx.coroutines.channels.SendChannel
import kotlin.js.JsString
import kotlin.js.toJsString

public open class Cursor internal constructor(
    internal open val cursor: IDBCursor,
    private val channel: SendChannel<*>,
) {
    public val key: IDBKey
        get() = cursor.key

    public val primaryKey: IDBKey
        get() = cursor.primaryKey

    public fun close() {
        channel.close()
    }

    public fun `continue`() {
        cursor.`continue`()
    }

    public fun advance(count: Int) {
        cursor.advance(count)
    }

    public fun `continue`(key: Key) {
        cursor.`continue`(key.toJs())
    }

    public fun continuePrimaryKey(key: Key, primaryKey: Key) {
        cursor.continuePrimaryKey(key.toJs(), primaryKey.toJs())
    }

    public enum class Direction(
        internal val constant: JsString,
    ) {
        Next("next".toJsString()),
        NextUnique("nextunique".toJsString()),
        Previous("prev".toJsString()),
        PreviousUnique("prevunique".toJsString()),
    }
}

public class CursorWithValue internal constructor(
    override val cursor: IDBCursorWithValue,
    channel: SendChannel<*>,
) : Cursor(cursor, channel) {
    public val value: IDBValue
        get() = cursor.value
}
