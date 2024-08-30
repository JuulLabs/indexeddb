package com.juul.indexeddb.external

/** https://developer.mozilla.org/en-US/docs/Web/API/IDBObjectStore */
public external interface IDBObjectStore : IDBQueryable {
    public val name: String

    public fun add(value: JsAny?, key: IDBKey = definedExternally): IDBRequest<IDBKey>

    public fun put(item: JsAny?, key: IDBKey = definedExternally): IDBRequest<IDBKey>

    public fun delete(key: IDBKey): IDBRequest<*>
    public fun delete(key: IDBKeyRange): IDBRequest<*>

    public fun clear(): IDBRequest<*>

    public fun index(name: String): IDBIndex
    public fun deleteIndex(name: String)

    public fun createIndex(
        name: String,
        keyPath: JsAny?,
        options: IDBIndexOptions = definedExternally,
    ): IDBIndex
}
