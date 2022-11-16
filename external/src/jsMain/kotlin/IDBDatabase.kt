package com.juul.indexeddb.external

import org.w3c.dom.events.EventTarget

/** https://developer.mozilla.org/en-US/docs/Web/API/IDBDatabase */
public external class IDBDatabase : EventTarget {
    public val name: String
    public val version: Int
    public val objectStoreNames: Array<String>
    public fun close()
    public fun createObjectStore(name: String): IDBObjectStore
    public fun createObjectStore(name: String, options: dynamic): IDBObjectStore
    public fun deleteObjectStore(name: String)
    public fun transaction(storeNames: Array<String>, mode: String): IDBTransaction
}
