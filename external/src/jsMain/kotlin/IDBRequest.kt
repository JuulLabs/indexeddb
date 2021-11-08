package com.juul.indexeddb.external

import org.w3c.dom.events.Event
import org.w3c.dom.events.EventTarget

/** https://developer.mozilla.org/en-US/docs/Web/API/IDBRequest */
public open external class IDBRequest<T> : EventTarget {
    public val error: Throwable?
    public val transaction: IDBTransaction?
    public val result: T
    public var onerror: (Event) -> Unit
    public var onsuccess: (Event) -> Unit
}

/** https://developer.mozilla.org/en-US/docs/Web/API/IDBOpenDBRequest */
public external class IDBOpenDBRequest : IDBRequest<IDBDatabase> {
    public var onblocked: (Event) -> Unit
    public var onupgradeneeded: (IDBVersionChangeEvent) -> Unit
}
