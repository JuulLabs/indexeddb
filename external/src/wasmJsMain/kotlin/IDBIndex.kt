package com.juul.indexeddb.external

public actual external interface IDBIndex : IDBQueryable {
    public actual val name: String
    public actual val objectStore: IDBObjectStore
}
