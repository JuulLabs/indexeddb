package com.juul.indexeddb.external

public actual external class IDBTransaction : EventTarget {
    public actual val objectStoreNames: JsArray<JsString> // Actually a DOMStringList
    public actual val db: IDBDatabase
    public actual val error: JsAny?
    public actual fun objectStore(name: String): IDBObjectStore
    public actual fun abort()
    public actual fun commit()
    public actual var onabort: (Event) -> Unit
    public actual var onerror: (Event) -> Unit
    public actual var oncomplete: (Event) -> Unit
}
