package com.juul.indexeddb.external

public actual external interface IDBFactory : JsAny {
    public actual fun open(name: String, version: Int): IDBOpenDBRequest
    public actual fun deleteDatabase(name: String): IDBOpenDBRequest
}
