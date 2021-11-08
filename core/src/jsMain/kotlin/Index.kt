package com.juul.indexeddb

import com.juul.indexeddb.external.IDBIndex

public class Index internal constructor(internal val index: IDBIndex) : Queryable {
    override suspend fun requestGet(key: Key): Request<dynamic> =
        Request(index.get(key.toJs()))

    override suspend fun requestGetAll(key: Key?): Request<Array<dynamic>> =
        Request(index.getAll(key?.toJs()))
}
