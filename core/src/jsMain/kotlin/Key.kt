package com.juul.indexeddb

import com.juul.indexeddb.external.IDBKeyRange
import kotlinext.js.jsObject

public object AutoIncrement {
    internal fun toJs(): dynamic = jsObject { autoIncrement = true }
}

public class KeyPath private constructor(
    private val paths: Array<String>,
) {
    init {
        require(paths.isNotEmpty())
    }

    public constructor(path: String, vararg morePaths: String) : this(arrayOf(path, *morePaths))

    internal fun toWrappedJs(): dynamic = jsObject { keyPath = if (paths.size == 1) paths[0] else paths }
    internal fun toUnwrappedJs(): dynamic = if (paths.size == 1) paths[0] else paths
}

public class Key private constructor(
    private val values: Array<dynamic>,
) {
    init {
        require(values.isNotEmpty())
    }

    public constructor(value: dynamic, vararg moreValues: dynamic) : this(arrayOf(value, *moreValues))

    internal fun toJs(): dynamic = if (values.size == 1) values[0] else values
}

public fun lowerBound(
    x: dynamic,
    open: Boolean = false,
): Key = Key(IDBKeyRange.lowerBound(x, open))

public fun upperBound(
    y: dynamic,
    open: Boolean = false,
): Key = Key(IDBKeyRange.upperBound(y, open))

public fun bound(
    x: dynamic,
    y: dynamic,
    lowerOpen: Boolean = false,
    upperOpen: Boolean = false,
): Key = Key(IDBKeyRange.bound(x, y, lowerOpen, upperOpen))
