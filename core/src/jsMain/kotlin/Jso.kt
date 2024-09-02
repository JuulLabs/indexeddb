package com.juul.indexeddb

import com.juul.indexeddb.external.JsAny

// Copied from:
// https://github.com/JetBrains/kotlin-wrappers/blob/91b2c1568ec6f779af5ec10d89b5e2cbdfe785ff/kotlin-extensions/src/main/kotlin/kotlinx/js/jso.kt

internal actual fun <T : JsAny> jso(): T = js("({})").unsafeCast<T>()
