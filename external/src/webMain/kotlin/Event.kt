package com.juul.indexeddb.external

import kotlin.js.JsAny

public external interface Event : JsAny {
    public val target: EventTarget
    public val type: String
}
