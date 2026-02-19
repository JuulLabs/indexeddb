package com.juul.indexeddb.external

/** https://developer.mozilla.org/en-US/docs/Web/API/IDBObjectStore */
public external interface IDBObjectStore : IDBQueryable {
    public val name: String

    public fun add(value: IDBValue): IDBRequest<IDBKey>
    public fun add(value: IDBValue, key: IDBKey): IDBRequest<IDBKey>

    public fun put(item: IDBValue): IDBRequest<IDBKey>
    public fun put(item: IDBValue, key: IDBKey): IDBRequest<IDBKey>

    public fun delete(key: IDBKey): IDBRequest<Nothing?>

    public fun clear(): IDBRequest<Nothing?>

    public fun index(name: String): IDBIndex
    public fun deleteIndex(name: String)
    public fun createIndex(name: String, keyPath: IDBKeyPath): IDBIndex
    public fun createIndex(name: String, keyPath: IDBKeyPath, options: IDBCreateIndexOptions): IDBIndex
}
