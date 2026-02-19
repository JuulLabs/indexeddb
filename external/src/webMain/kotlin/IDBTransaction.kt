package com.juul.indexeddb.external

/** https://developer.mozilla.org/en-US/docs/Web/API/IDBTransaction */
public external interface IDBTransaction : EventTarget {
    public val objectStoreNames: DOMStringList
    public val db: IDBDatabase
    public val durability: IDBDurability
    public val error: DOMException?
    public val mode: IDBTransactionMode
    public fun objectStore(name: String): IDBObjectStore
    public fun abort()
    public fun commit()
    public var onabort: (Event) -> Unit
    public var onerror: (Event) -> Unit
    public var oncomplete: (Event) -> Unit
}
