package com.juul.indexeddb

import com.juul.indexeddb.external.IDBFactory
import com.juul.indexeddb.external.JsAny
import com.juul.indexeddb.external.JsArray
import com.juul.indexeddb.external.ReadonlyArray
import kotlin.js.unsafeCast as jsUnsafeCast

internal actual val selfIndexedDB: IDBFactory? = run {
    val indexedDB: dynamic = js("self.indexedDB || self.webkitIndexedDB")
    indexedDB?.jsUnsafeCast<IDBFactory>()
}

internal actual fun <T : JsAny?> JsArray<T>.toReadonlyArray(): ReadonlyArray<T> = jsUnsafeCast<ReadonlyArray<T>>()

internal actual fun <T : JsAny?> JsAny.unsafeCast(): T = jsUnsafeCast<T>()
