package com.juul.indexeddb

import com.juul.indexeddb.external.IDBCursor
import com.juul.indexeddb.external.IDBCursorWithValue
import com.juul.indexeddb.external.IDBObjectStore
import com.juul.indexeddb.external.IDBValue
import kotlin.js.JsArray
import kotlin.js.JsNumber

public class ObjectStore internal constructor(
    internal val objectStore: IDBObjectStore,
) : Queryable() {

    override val type: String
        get() = "object store"

    override val name: String
        get() = objectStore.name

    override fun requestGet(key: Key): Request<IDBValue> =
        Request(objectStore.get(key.toJs()))

    override fun requestGetAll(query: Key?, count: UInt?): Request<JsArray<IDBValue>> = when {
        query == null -> Request(objectStore.getAll())
        count == null -> Request(objectStore.getAll(query.toJs()))
        else -> Request(objectStore.getAll(query.toJs(), count.toInt()))
    }

    override fun requestOpenCursor(query: Key?, direction: Cursor.Direction?): Request<IDBCursorWithValue?> = when {
        query == null -> Request(objectStore.openCursor())
        direction == null -> Request(objectStore.openCursor(query.toJs()))
        else -> Request(objectStore.openCursor(query.toJs(), direction.constant))
    }

    override fun requestOpenKeyCursor(query: Key?, direction: Cursor.Direction?): Request<IDBCursor?> = when {
        query == null -> Request(objectStore.openKeyCursor())
        direction == null -> Request(objectStore.openKeyCursor(query.toJs()))
        else -> Request(objectStore.openKeyCursor(query.toJs(), direction.constant))
    }

    override fun requestCount(query: Key?): Request<JsNumber> = when {
        query == null -> Request(objectStore.count())
        else -> Request(objectStore.count(query.toJs()))
    }
}
