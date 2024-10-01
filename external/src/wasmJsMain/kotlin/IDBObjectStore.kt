package com.juul.indexeddb.external

public actual external interface IDBObjectStore : IDBQueryable {
    public actual val name: String

    public actual fun add(value: JsAny?): IDBRequest<IDBKey>
    public actual fun add(value: JsAny?, key: IDBKey): IDBRequest<IDBKey>

    public actual fun put(item: JsAny?): IDBRequest<IDBKey>
    public actual fun put(item: JsAny?, key: IDBKey): IDBRequest<IDBKey>

    public actual fun delete(key: IDBKey): IDBRequest<*>
    public actual fun delete(key: IDBKeyRange): IDBRequest<*>

    public actual fun clear(): IDBRequest<*>

    public actual fun index(name: String): IDBIndex
    public actual fun deleteIndex(name: String)
    public actual fun createIndex(name: String, keyPath: JsAny?, options: IDBIndexOptions): IDBIndex
}
