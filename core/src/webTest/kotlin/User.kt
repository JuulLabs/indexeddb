package com.juul.indexeddb

import kotlin.js.JsAny
import kotlin.js.JsString

/** Example IDB friendly JS type. */
internal external interface User : JsAny {
    var id: JsString?
    var username: JsString?
}
