package com.juul.indexeddb.external

import kotlin.js.JsAny

public external interface EventTarget : JsAny {
    public fun addEventListener(type: String, listener: (Event) -> Unit)
    public fun removeEventListener(type: String, listener: (Event) -> Unit)
}
