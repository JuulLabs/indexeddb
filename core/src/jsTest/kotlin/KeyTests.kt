package com.juul.indexeddb

import com.juul.indexeddb.external.IDBKeyRange
import kotlinext.js.jsObject
import kotlin.js.Date
import kotlin.test.Test
import kotlin.test.assertFails

public class KeyTests {

    @Test
    public fun constructor_withObjectType_shouldFail() {
        assertFails { Key(jsObject()) }
    }

    @Test
    public fun constructor_withArrayOfObjectType_shouldFail() {
        assertFails { Key(arrayOf<dynamic>(jsObject())) }
    }

    @Test
    public fun constructor_withLong_shouldFail() {
        assertFails { Key(4L) }
    }

    @Test
    public fun constructor_withString_completes() {
        Key("string")
    }

    @Test
    public fun constructor_withDate_completes() {
        Key(Date("2021-11-11T12:00:00"))
    }

    @Test
    public fun constructor_withNiceNumbers_completes() {
        Key(1, 2f, 3.0)
    }

    @Test
    public fun constructor_withByteArray_completes() {
        Key(byteArrayOf(1, 2, 3, 4, 5, 6))
    }

    @Test
    public fun constructor_withArrayOfString_completes() {
        Key(arrayOf(arrayOf("foo"), "bar"))
    }

    @Test
    public fun constructor_withRange_completes() {
        Key(IDBKeyRange.upperBound("foobar", false))
    }

    @Test
    public fun constructor_withNull_completes() {
        Key(null)
    }
}
