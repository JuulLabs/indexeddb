package com.juul.indexeddb.external

public actual external class IDBDatabase : EventTarget {
    public actual val name: String
    public actual val version: Int
    public actual val objectStoreNames: JsArray<JsString>
    public actual fun close()
    public actual fun createObjectStore(name: String): IDBObjectStore
    public actual fun createObjectStore(name: String, options: IDBObjectStoreOptions?): IDBObjectStore
    public actual fun deleteObjectStore(name: String)

    public actual fun transaction(
        storeName: String,
        mode: String,
        options: IDBTransactionOptions,
    ): IDBTransaction

    public actual fun transaction(
        storeNames: ReadonlyArray<JsString>,
        mode: String,
        options: IDBTransactionOptions,
    ): IDBTransaction
}
