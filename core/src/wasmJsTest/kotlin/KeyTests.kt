package com.juul.indexeddb

import com.juul.indexeddb.external.IDBKeyRange
import org.khronos.webgl.Uint8Array
import kotlin.test.Test
import kotlin.test.assertFails

public class KeyTests {

    @Test
    public fun constructor_withObjectType_shouldFail() {
        assertFails { Key(jso()) }
    }

    @Test
    public fun constructor_withArrayOfObjectType_shouldFail() {
        assertFails { Key(JsArray(jso())) }
    }

    @Test
    public fun constructor_withLong_shouldFail() {
        assertFails { Key(4L.toJsBigInt()) }
    }

    @Test
    public fun constructor_withString_completes() {
        Key("string".toJsString())
    }

    @Test
    public fun constructor_withDate_completes() {
        Key(Date("2021-11-11T12:00:00".toJsString()))
    }

    @Test
    public fun constructor_withNiceNumbers_completes() {
        Key(1.toJsNumber(), 3.0.toJsNumber())
    }

    @Test
    public fun constructor_withByteArray_completes() {
        Key(
            Uint8Array(
                JsArray(
                    1.toJsNumber(),
                    2.toJsNumber(),
                    3.toJsNumber(),
                    4.toJsNumber(),
                    5.toJsNumber(),
                    6.toJsNumber(),
                ),
            ),
        )
    }

    @Test
    public fun constructor_withArrayOfString_completes() {
        Key(JsArray(JsArray("foo".toJsString()), "bar".toJsString()))
    }

    @Test
    public fun constructor_withRange_completes() {
        Key(IDBKeyRange.upperBound("foobar".toJsString(), false))
    }

    @Test
    public fun constructor_withNull_completes() {
        Key(null)
    }
}
