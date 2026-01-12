package com.juul.indexeddb.external

import kotlin.js.JsAny
import kotlin.js.JsBoolean

/**
 * Must be one of the types supported by [IDBKey] other than [IDBKeyRange], or one of [boolean][JsBoolean], `null`,
 * `undefined`, a native javascript regex, or a plain javascript object using only [IDBValue] compatible values.
 */
public typealias IDBValue = JsAny?
