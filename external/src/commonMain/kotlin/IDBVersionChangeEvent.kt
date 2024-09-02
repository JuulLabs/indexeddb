package com.juul.indexeddb.external

/** https://developer.mozilla.org/en-US/docs/Web/API/IDBVersionChangeEvent */
public expect abstract class IDBVersionChangeEvent : Event {
    public val oldVersion: Int
    public val newVersion: Int
}
