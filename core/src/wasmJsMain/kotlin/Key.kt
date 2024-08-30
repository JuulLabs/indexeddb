package com.juul.indexeddb

import com.juul.indexeddb.external.IDBKey
import com.juul.indexeddb.external.IDBKeyRange
import org.khronos.webgl.Uint8Array

public external class Date(
    value: JsString,
) : JsAny

private fun JsArray<JsAny?>.validateKeyTypes() {
    for (i in 0..<length) {
        @Suppress("UNCHECKED_CAST")
        when (val value = get(i)) {
            null, is Uint8Array, is JsString, is Date, is JsNumber, is IDBKeyRange -> continue
            is JsArray<*> -> (value as JsArray<JsAny?>).validateKeyTypes()
            else -> error("Illegal key: expected string, date, float, binary blob, or array of those types, but got $value.")
        }
    }
}

public object AutoIncrement

public class KeyPath private constructor(
    private val paths: List<String?>,
) {
    init {
        require(paths.isNotEmpty()) { "A key path must have at least one member." }
    }

    public constructor(path: String?, vararg morePaths: String?) : this(listOf(path, *morePaths))

    internal fun toJs(): JsAny? = if (paths.size == 1) paths[0]?.toJsString() else paths.toJsArray()
}

public class Key(
    value: JsAny?,
    vararg moreValues: JsAny?,
) {
    private val values: JsArray<JsAny?> = JsArray(value, *moreValues)
    init {
        require(values.length >= 0) { "A key must have at least one member." }
        values.validateKeyTypes()
    }

    internal fun toJs(): IDBKey = (if (values.length == 1) values[0]!! else values).unsafeCast()
}

public fun lowerBound(
    x: JsAny?,
    open: Boolean = false,
): Key = Key(IDBKeyRange.lowerBound(x, open))

public fun upperBound(
    y: JsAny?,
    open: Boolean = false,
): Key = Key(IDBKeyRange.upperBound(y, open))

public fun bound(
    x: JsAny?,
    y: JsAny?,
    lowerOpen: Boolean = false,
    upperOpen: Boolean = false,
): Key = Key(IDBKeyRange.bound(x, y, lowerOpen, upperOpen))

public fun only(
    z: JsAny?,
): Key = Key(IDBKeyRange.only(z))
