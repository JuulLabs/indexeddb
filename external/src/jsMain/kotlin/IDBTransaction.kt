package com.juul.indexeddb.external

import org.w3c.dom.events.Event
import org.w3c.dom.events.EventTarget

/** https://developer.mozilla.org/en-US/docs/Web/API/IDBTransaction */
public external class IDBTransaction : EventTarget {
    public val objectStoreNames: Array<String> // Actually a DOMStringList
    public val db: IDBDatabase
    public val error: Throwable?
    public fun objectStore(name: String): IDBObjectStore
    public fun abort(): Unit
    public fun commit(): Unit
    public var onabort: (Event) -> Unit
    public var onerror: (Event) -> Unit
    public var oncomplete: (Event) -> Unit
}
