package com.juul.indexeddb

import com.juul.indexeddb.external.IDBFactory
import com.juul.indexeddb.external.JsAny
import com.juul.indexeddb.external.JsArray
import com.juul.indexeddb.external.JsByteArray
import com.juul.indexeddb.external.ReadonlyArray
import com.juul.indexeddb.external.toJsByteArray

internal expect val selfIndexedDB: IDBFactory?

internal expect fun <T : JsAny?> JsArray<T>.toReadonlyArray(): ReadonlyArray<T>

internal expect fun <T : JsAny?> JsAny.unsafeCast(): T

internal fun ByteArray.toJsByteArray(): JsByteArray = toTypedArray().toJsByteArray()
