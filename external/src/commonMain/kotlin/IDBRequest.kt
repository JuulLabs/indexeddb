package com.juul.indexeddb.external

/** https://developer.mozilla.org/en-US/docs/Web/API/IDBRequest */
public expect open class IDBRequest<T : JsAny?> : EventTarget {
    public val error: JsAny?
    public val transaction: IDBTransaction?
    public val result: T
    public var onerror: (Event) -> Unit
    public var onsuccess: (Event) -> Unit
}

/** https://developer.mozilla.org/en-US/docs/Web/API/IDBOpenDBRequest */
public expect class IDBOpenDBRequest : IDBRequest<IDBDatabase> {
    public var onblocked: (Event) -> Unit
    public var onupgradeneeded: (IDBVersionChangeEvent) -> Unit
}
