package com.juul.indexeddb

import com.juul.indexeddb.external.IDBCursor
import com.juul.indexeddb.external.IDBCursorWithValue
import com.juul.indexeddb.external.IDBValue
import kotlin.js.JsArray
import kotlin.js.JsNumber

public sealed class Queryable {
    /** Either "index" or "object store". */
    internal abstract val type: String
    internal abstract val name: String
    internal abstract fun requestGet(key: Key): Request<IDBValue>
    internal abstract fun requestGetAll(query: Key?, count: UInt?): Request<JsArray<IDBValue>>
    internal abstract fun requestOpenCursor(query: Key?, direction: Cursor.Direction?): Request<IDBCursorWithValue?>
    internal abstract fun requestOpenKeyCursor(query: Key?, direction: Cursor.Direction?): Request<IDBCursor?>
    internal abstract fun requestCount(query: Key?): Request<JsNumber>
}
