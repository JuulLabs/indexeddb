package com.juul.indexeddb.external

// Copied from:
// https://github.com/JetBrains/kotlin-wrappers/blob/91b2c1568ec6f779af5ec10d89b5e2cbdfe785ff/kotlin-extensions/src/main/kotlin/kotlinx/js/jso.kt

internal fun <T : JsAny> jso(): T = js("({})").unsafeCast<T>()
internal fun <T : JsAny> jso(block: T.() -> Unit): T = jso<T>().apply(block)
