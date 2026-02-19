package com.juul.indexeddb

import com.juul.indexeddb.external.IDBCursor
import com.juul.indexeddb.external.IDBCursorWithValue
import com.juul.indexeddb.external.IDBIndex
import com.juul.indexeddb.external.IDBValue
import kotlin.js.JsArray
import kotlin.js.JsNumber

public class Index internal constructor(
    internal val index: IDBIndex,
) : Queryable() {

    override val type: String
        get() = "index"

    override val name: String
        get() = index.name

    override fun requestGet(key: Key): Request<IDBValue> =
        Request(index.get(key.toJs()))

    override fun requestGetAll(query: Key?, count: UInt?): Request<JsArray<IDBValue>> = when {
        query == null -> Request(index.getAll())
        count == null -> Request(index.getAll(query.toJs()))
        else -> Request(index.getAll(query.toJs(), count.toInt()))
    }

    override fun requestOpenCursor(query: Key?, direction: Cursor.Direction?): Request<IDBCursorWithValue?> = when {
        query == null -> Request(index.openCursor())
        direction == null -> Request(index.openCursor(query.toJs()))
        else -> Request(index.openCursor(query.toJs(), direction.constant))
    }

    override fun requestOpenKeyCursor(query: Key?, direction: Cursor.Direction?): Request<IDBCursor?> = when {
        query == null -> Request(index.openKeyCursor())
        direction == null -> Request(index.openKeyCursor(query.toJs()))
        else -> Request(index.openKeyCursor(query.toJs(), direction.constant))
    }

    override fun requestCount(query: Key?): Request<JsNumber> = when {
        query == null -> Request(index.count())
        else -> Request(index.count(query.toJs()))
    }
}
