package com.juul.indexeddb.external

/** https://developer.mozilla.org/en-US/docs/Web/API/IDBFactory */
public expect interface IDBFactory {
    public fun open(name: String, version: Int): IDBOpenDBRequest
    public fun deleteDatabase(name: String): IDBOpenDBRequest
}
