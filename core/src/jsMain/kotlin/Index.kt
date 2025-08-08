package com.juul.indexeddb

import com.juul.indexeddb.external.IDBCursor
import com.juul.indexeddb.external.IDBCursorWithValue
import com.juul.indexeddb.external.IDBIndex

public class Index internal constructor(
    internal val index: IDBIndex,
) : Queryable() {

    override val type: String
        get() = "index"

    override val name: String
        get() = index.name

    override fun requestGet(key: Key): Request<dynamic> =
        Request(index.get(key.toJs()))

    override fun requestGetAll(query: Key?, count: UInt?): Request<Array<dynamic>> = if (count == null) {
        Request(index.getAll(query?.toJs()))
    } else {
        Request(index.getAll(query?.toJs(), count.toDouble()))
    }

    override fun requestOpenCursor(query: Key?, direction: Cursor.Direction): Request<IDBCursorWithValue?> =
        Request(index.openCursor(query?.toJs(), direction.constant))

    override fun requestOpenKeyCursor(query: Key?, direction: Cursor.Direction): Request<IDBCursor?> =
        Request(index.openKeyCursor(query?.toJs(), direction.constant))

    override fun requestCount(query: Key?): Request<Int> =
        Request(index.count(query?.toJs()))
}
