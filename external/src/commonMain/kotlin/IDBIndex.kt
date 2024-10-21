package com.juul.indexeddb.external

/** https://developer.mozilla.org/en-US/docs/Web/API/IDBIndex */
public expect interface IDBIndex : IDBQueryable {
    public val name: String
    public val objectStore: IDBObjectStore
}
