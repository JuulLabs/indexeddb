package com.juul.indexeddb.external

import kotlin.js.JsAny
import kotlin.js.JsArray
import kotlin.js.Promise
import kotlin.js.js

/** https://developer.mozilla.org/en-US/docs/Web/API/IDBFactory */
public external class IDBFactory : JsAny {
    public fun open(name: String, version: Int): IDBOpenDBRequest
    public fun databases(): Promise<JsArray<IDBAvailableDatabase>>
    public fun deleteDatabase(name: String): IDBOpenDBRequest
}

public val indexedDB: IDBFactory? = js("self.indexedDB || self.webkitIndexedDB")
