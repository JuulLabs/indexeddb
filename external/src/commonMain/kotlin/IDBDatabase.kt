package com.juul.indexeddb.external

/** https://developer.mozilla.org/en-US/docs/Web/API/IDBDatabase */
public expect class IDBDatabase : EventTarget {
    public val name: String
    public val version: Int
    public val objectStoreNames: JsArray<JsString>
    public fun close()
    public fun createObjectStore(name: String): IDBObjectStore
    public fun createObjectStore(name: String, options: IDBObjectStoreOptions?): IDBObjectStore
    public fun deleteObjectStore(name: String)

    public fun transaction(
        storeName: String,
        mode: String,
        options: IDBTransactionOptions,
    ): IDBTransaction

    public fun transaction(
        storeNames: ReadonlyArray<JsString>,
        mode: String,
        options: IDBTransactionOptions,
    ): IDBTransaction
}
