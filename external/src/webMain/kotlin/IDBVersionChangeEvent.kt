package com.juul.indexeddb.external

/** https://developer.mozilla.org/en-US/docs/Web/API/IDBVersionChangeEvent */
public external class IDBVersionChangeEvent : Event {
    public val oldVersion: Int
    public val newVersion: Int
    override val target: EventTarget
    override val type: String
}
