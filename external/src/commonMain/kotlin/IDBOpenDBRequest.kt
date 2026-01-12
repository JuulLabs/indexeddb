package com.juul.indexeddb.external

/** https://developer.mozilla.org/en-US/docs/Web/API/IDBOpenDBRequest */
public external interface IDBOpenDBRequest : IDBRequest<IDBDatabase> {
    public var onblocked: (Event) -> Unit
    public var onupgradeneeded: (IDBVersionChangeEvent) -> Unit
}
