package com.juul.indexeddb

import com.juul.indexeddb.external.IDBCursor
import com.juul.indexeddb.external.IDBCursorWithValue
import com.juul.indexeddb.external.IDBObjectStore
import com.juul.indexeddb.external.ReadonlyArray

public class ObjectStore internal constructor(
    internal val objectStore: IDBObjectStore,
) : Queryable() {
    override fun requestGet(key: Key): Request<*> =
        Request(objectStore.get(key.toJs()))

    override fun requestGetAll(query: Key?): Request<ReadonlyArray<*>> =
        Request(objectStore.getAll(query?.toJs()))

    override fun requestOpenCursor(query: Key?, direction: Cursor.Direction): Request<IDBCursorWithValue?> =
        Request(objectStore.openCursor(query?.toJs(), direction.constant))

    override fun requestOpenKeyCursor(query: Key?, direction: Cursor.Direction): Request<IDBCursor?> =
        Request(objectStore.openKeyCursor(query?.toJs(), direction.constant))

    override fun requestCount(query: Key?): Request<JsNumber> =
        Request(objectStore.count(query?.toJs()))
}
