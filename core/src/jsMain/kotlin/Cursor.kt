package com.juul.indexeddb

import com.juul.indexeddb.external.IDBCursor
import com.juul.indexeddb.external.IDBCursorWithValue

public open class Cursor internal constructor(
    internal open val cursor: IDBCursor,
) {
    public val key: dynamic
        get() = cursor.key

    public val primaryKey: dynamic
        get() = cursor.primaryKey

    public enum class Direction(internal val constant: String) {
        Next("next"),
        NextUnique("nextunique"),
        Previous("prev"),
        PreviousUnique("prevunique")
    }
}

public class CursorWithValue internal constructor(
    override val cursor: IDBCursorWithValue,
) : Cursor(cursor) {
    public val value: dynamic
        get() = cursor.value
}
