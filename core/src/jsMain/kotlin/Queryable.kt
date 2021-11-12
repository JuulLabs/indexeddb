package com.juul.indexeddb

import com.juul.indexeddb.external.IDBCursor
import com.juul.indexeddb.external.IDBCursorWithValue

public sealed class Queryable {
    internal abstract fun requestGet(key: Key): Request<dynamic>
    internal abstract fun requestGetAll(query: Key?): Request<Array<dynamic>>
    internal abstract fun requestOpenCursor(query: Key?, direction: Cursor.Direction): Request<IDBCursorWithValue?>
    internal abstract fun requestOpenKeyCursor(query: Key?, direction: Cursor.Direction): Request<IDBCursor?>
}
