package com.juul.indexeddb.external

import kotlin.js.JsArray
import kotlin.js.JsString

/** https://developer.mozilla.org/en-US/docs/Web/API/IDBDatabase */
public external interface IDBDatabase : EventTarget {
    public val name: String
    public val version: Int
    public val objectStoreNames: JsArray<JsString>
    public fun close()
    public fun createObjectStore(name: String): IDBObjectStore
    public fun createObjectStore(name: String, options: IDBCreateObjectStoreOptions): IDBObjectStore
    public fun deleteObjectStore(name: String)
    public fun transaction(storeNames: JsArray<JsString>, mode: String, options: IDBTransactionOptions): IDBTransaction
}
