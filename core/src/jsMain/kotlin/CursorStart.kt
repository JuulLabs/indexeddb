package com.juul.indexeddb

import com.juul.indexeddb.external.IDBCursor

public sealed class CursorStart {

    internal abstract fun apply(cursor: IDBCursor)

    public data class Advance(val count: Int) : CursorStart() {
        override fun apply(cursor: IDBCursor) {
            cursor.advance(count)
        }
    }

    public data class Continue(val key: Key) : CursorStart() {
        override fun apply(cursor: IDBCursor) {
            cursor.`continue`(key.toJs())
        }
    }

    public data class ContinuePrimaryKey(val key: Key, val primaryKey: Key) : CursorStart() {
        override fun apply(cursor: IDBCursor) {
            cursor.continuePrimaryKey(key.toJs(), primaryKey.toJs())
        }
    }
}