package com.juul.indexeddb.external

/** https://developer.mozilla.org/en-US/docs/Web/API/IDBObjectStore */
public external interface IDBObjectStore : IDBQueryable {
    public val name: String

    public fun add(value: dynamic): IDBRequest<dynamic>
    public fun add(value: dynamic, key: dynamic): IDBRequest<dynamic>

    public fun put(item: dynamic): IDBRequest<dynamic>
    public fun put(item: dynamic, key: dynamic): IDBRequest<dynamic>

    public fun delete(key: dynamic): IDBRequest<dynamic>

    public fun clear(): IDBRequest<dynamic>

    public fun index(name: String): IDBIndex
    public fun deleteIndex(name: String): Unit
    public fun createIndex(name: String, keyPath: dynamic, parameters: dynamic): IDBIndex
}
