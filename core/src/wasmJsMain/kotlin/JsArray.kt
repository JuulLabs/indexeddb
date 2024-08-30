package com.juul.indexeddb

import com.juul.indexeddb.external.ReadonlyArray

internal fun Iterable<String?>.toJsArray(): JsArray<JsString?> =
    kotlin.js.JsArray<JsString?>().apply {
        forEachIndexed { index, s ->
            set(index, s?.toJsString())
        }
    }

internal fun <T : JsAny?> JsArray(vararg values: T): JsArray<T> =
    kotlin.js.JsArray<T>().apply {
        for (i in values.indices) {
            set(i, values[i])
        }
    }

internal fun <T : JsAny?> ReadonlyArray(vararg values: T): ReadonlyArray<T> =
    kotlin.js.JsArray<T>().apply {
        for (i in values.indices) {
            set(i, values[i])
        }
    }
