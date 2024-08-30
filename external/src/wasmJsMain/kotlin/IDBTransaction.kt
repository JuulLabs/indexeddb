package com.juul.indexeddb.external

import org.w3c.dom.events.Event
import org.w3c.dom.events.EventTarget

/** https://developer.mozilla.org/en-US/docs/Web/API/IDBTransaction */
public external class IDBTransaction : EventTarget {
    public val objectStoreNames: JsArray<JsString> // Actually a DOMStringList
    public val db: IDBDatabase
    public val error: JsAny?
    public fun objectStore(name: String): IDBObjectStore
    public fun abort()
    public fun commit()
    public var onabort: (Event) -> Unit
    public var onerror: (Event) -> Unit
    public var oncomplete: (Event) -> Unit
}
