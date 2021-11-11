package com.juul.indexeddb

import com.juul.indexeddb.external.IDBCursor
import com.juul.indexeddb.external.IDBCursorWithValue

public sealed interface Queryable {
    public fun requestGet(key: Key): Request<dynamic>
    public fun requestGetAll(query: Key?): Request<Array<dynamic>>
    public fun requestOpenCursor(query: Key?, direction: Cursor.Direction): Request<IDBCursorWithValue?>
    public fun requestOpenKeyCursor(query: Key?, direction: Cursor.Direction): Request<IDBCursor?>
}
