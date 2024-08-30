package com.juul.indexeddb.external

import org.w3c.dom.events.Event

/** https://developer.mozilla.org/en-US/docs/Web/API/IDBVersionChangeEvent */
public abstract external class IDBVersionChangeEvent : Event {
    public val oldVersion: Int
    public val newVersion: Int
}
