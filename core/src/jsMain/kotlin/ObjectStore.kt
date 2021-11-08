package com.juul.indexeddb

import com.juul.indexeddb.external.IDBObjectStore

public class ObjectStore internal constructor(
    internal val objectStore: IDBObjectStore,
) : Queryable {
    override suspend fun requestGet(key: Key): Request<dynamic> =
        Request(objectStore.get(key.toJs()))

    override suspend fun requestGetAll(key: Key?): Request<Array<dynamic>> =
        Request(objectStore.getAll(key?.toJs()))
}
