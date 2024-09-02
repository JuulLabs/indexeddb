package com.juul.indexeddb

import com.juul.indexeddb.external.IDBFactory
import com.juul.indexeddb.external.ReadonlyArray
import kotlin.js.unsafeCast as jsUnsafeCast

internal actual val selfIndexedDB: IDBFactory? = js("self.indexedDB || self.webkitIndexedDB")

internal actual fun <T : JsAny?> JsArray<T>.toReadonlyArray(): ReadonlyArray<T> = unsafeCast()

internal actual fun <T : JsAny?> JsAny.unsafeCast(): T = jsUnsafeCast()
