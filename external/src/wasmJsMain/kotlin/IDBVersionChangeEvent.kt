package com.juul.indexeddb.external

public actual abstract external class IDBVersionChangeEvent : Event {
    public actual val oldVersion: Int
    public actual val newVersion: Int
}
