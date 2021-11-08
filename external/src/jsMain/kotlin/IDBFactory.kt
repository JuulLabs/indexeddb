package com.juul.indexeddb.external

import org.w3c.dom.Window

/** https://developer.mozilla.org/en-US/docs/Web/API/IDBFactory */
public external interface IDBFactory {
    public fun open(name: String, version: Int): IDBOpenDBRequest
    public fun deleteDatabase(name: String): IDBOpenDBRequest
}

@Suppress("UNCHECKED_CAST_TO_EXTERNAL_INTERFACE")
public val Window.indexedDB: IDBFactory?
    get() = this.asDynamic().indexedDB as? IDBFactory
