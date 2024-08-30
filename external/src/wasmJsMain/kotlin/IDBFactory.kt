package com.juul.indexeddb.external

import org.w3c.dom.Window

/** https://developer.mozilla.org/en-US/docs/Web/API/IDBFactory */
public external interface IDBFactory : JsAny {
    public fun open(name: String, version: Int = definedExternally): IDBOpenDBRequest
    public fun deleteDatabase(name: String): IDBOpenDBRequest
}

public val Window.indexedDB: IDBFactory? get() = com.juul.indexeddb.external.indexedDB

@Suppress("RedundantNullableReturnType")
public val indexedDB: IDBFactory? = js("window.indexedDB")
