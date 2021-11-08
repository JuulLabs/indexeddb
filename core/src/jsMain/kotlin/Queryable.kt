package com.juul.indexeddb

public interface Queryable {
    public suspend fun requestGet(key: Key): Request<dynamic>
    public suspend fun requestGetAll(key: Key?): Request<Array<dynamic>>
}
