package com.juul.indexeddb

import com.juul.indexeddb.external.IDBCursor
import com.juul.indexeddb.external.IDBCursorWithValue
import com.juul.indexeddb.external.ReadonlyArray

public sealed class Queryable {
    internal abstract fun requestGet(key: Key): Request<*>
    internal abstract fun requestGetAll(query: Key?): Request<ReadonlyArray<*>>
    internal abstract fun requestOpenCursor(query: Key?, direction: Cursor.Direction): Request<IDBCursorWithValue?>
    internal abstract fun requestOpenKeyCursor(query: Key?, direction: Cursor.Direction): Request<IDBCursor?>
    internal abstract fun requestCount(query: Key?): Request<JsNumber>
}
