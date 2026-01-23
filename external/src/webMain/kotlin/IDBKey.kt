package com.juul.indexeddb.external

import kotlin.js.JsAny
import kotlin.js.JsNumber
import kotlin.js.JsString

/**
 * Must be any of [string][JsString], [number][JsNumber], `date`, `Blob`, arrays of this or the previous types, or
 * an [IDBKeyRange].
 */
public typealias IDBKey = JsAny
