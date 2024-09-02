package com.juul.indexeddb.external

public actual open external class IDBRequest<T : JsAny?> : EventTarget {
    public actual val error: JsAny?
    public actual val transaction: IDBTransaction?
    public actual val result: T
    public actual var onerror: (Event) -> Unit
    public actual var onsuccess: (Event) -> Unit
}

public actual external class IDBOpenDBRequest : IDBRequest<IDBDatabase> {
    public actual var onblocked: (Event) -> Unit
    public actual var onupgradeneeded: (IDBVersionChangeEvent) -> Unit
}
