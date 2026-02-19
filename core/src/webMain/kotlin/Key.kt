package com.juul.indexeddb

import com.juul.indexeddb.external.IDBKey
import com.juul.indexeddb.external.IDBKeyRange
import kotlin.js.toJsArray

public class Key(
    private val value: IDBKey,
    private vararg val moreValues: IDBKey,
) {
    internal fun toJs(): IDBKey = when (moreValues.isEmpty()) {
        true -> value
        false -> arrayOf(value, *moreValues).toJsArray()
    }
}

public fun lowerBound(
    x: IDBKey,
    open: Boolean = false,
): Key = Key(IDBKeyRange.lowerBound(x, open))

public fun upperBound(
    y: IDBKey,
    open: Boolean = false,
): Key = Key(IDBKeyRange.upperBound(y, open))

public fun bound(
    x: IDBKey,
    y: IDBKey,
    lowerOpen: Boolean = false,
    upperOpen: Boolean = false,
): Key = Key(IDBKeyRange.bound(x, y, lowerOpen, upperOpen))

public fun only(
    z: IDBKey,
): Key = Key(IDBKeyRange.only(z))
