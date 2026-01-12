package com.juul.indexeddb.external

import kotlin.js.JsAny

public external class DOMStringList : JsAny {
    public fun contains(name: String): Boolean
    public fun item(index: Int): String?
    public val length: Int
}

public fun DOMStringList.toKotlin(): List<String> {
    val buffer = ArrayList<String>(initialCapacity = length)
    for (index in 0 until length) {
        buffer[index] = item(index)!!
    }
    return buffer
}
