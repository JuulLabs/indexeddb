@file:Suppress("ktlint:standard:function-naming")

package com.juul.indexeddb

import com.juul.indexeddb.external.IDBCursor
import com.juul.indexeddb.external.IDBCursorWithValue
import kotlinx.coroutines.channels.SendChannel

public open class Cursor internal constructor(
    internal open val cursor: IDBCursor,
    private val channel: SendChannel<*>,
) {
    public val key: dynamic
        get() = cursor.key

    public val primaryKey: dynamic
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

    public enum class Direction(internal val constant: String) {
        Next("next"),
        NextUnique("nextunique"),
        Previous("prev"),
        PreviousUnique("prevunique"),
    }
}

public class CursorWithValue internal constructor(
    override val cursor: IDBCursorWithValue,
    channel: SendChannel<*>,
) : Cursor(cursor, channel) {
    public val value: dynamic
        get() = cursor.value
}
