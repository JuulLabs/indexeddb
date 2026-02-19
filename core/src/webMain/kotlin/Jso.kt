@file:Suppress("NOTHING_TO_INLINE")

package com.juul.indexeddb

import kotlin.js.JsAny
import kotlin.js.js

// Copied from:
// https://github.com/JetBrains/kotlin-wrappers/blob/91b2c1568ec6f779af5ec10d89b5e2cbdfe785ff/kotlin-extensions/src/main/kotlin/kotlinx/js/jso.kt

internal fun <T : JsAny> jso(): T = js("({})")
internal inline fun <T : JsAny> jso(block: T.() -> Unit): T = jso<T>().apply(block)
