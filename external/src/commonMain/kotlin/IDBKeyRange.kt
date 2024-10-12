package com.juul.indexeddb.external

/** https://developer.mozilla.org/en-US/docs/Web/API/IDBKeyRange */
public expect class IDBKeyRange : JsAny {

    public fun includes(value: JsAny?): Boolean

    public companion object {
        public fun lowerBound(x: JsAny?, open: Boolean): IDBKeyRange
        public fun upperBound(y: JsAny?, open: Boolean): IDBKeyRange
        public fun bound(x: JsAny?, y: JsAny?, lowerOpen: Boolean, upperOpen: Boolean): IDBKeyRange
        public fun only(z: JsAny?): IDBKeyRange
    }
}

public fun IDBKeyRange.Companion.lowerBound(x: String?, open: Boolean): IDBKeyRange =
    lowerBound(x?.toJsString(), open)

public fun IDBKeyRange.Companion.lowerBound(x: Int, open: Boolean): IDBKeyRange =
    lowerBound(x.toJsNumber(), open)

public fun IDBKeyRange.Companion.lowerBound(x: Double, open: Boolean): IDBKeyRange =
    lowerBound(x.toJsNumber(), open)

public fun IDBKeyRange.Companion.upperBound(x: String?, open: Boolean): IDBKeyRange =
    upperBound(x?.toJsString(), open)

public fun IDBKeyRange.Companion.upperBound(x: Int, open: Boolean): IDBKeyRange =
    upperBound(x.toJsNumber(), open)

public fun IDBKeyRange.Companion.upperBound(x: Double, open: Boolean): IDBKeyRange =
    upperBound(x.toJsNumber(), open)

public fun IDBKeyRange.Companion.bound(
    x: String?,
    y: String?,
    lowerOpen: Boolean,
    upperOpen: Boolean,
): IDBKeyRange = bound(x?.toJsString(), y?.toJsString(), lowerOpen, upperOpen)

public fun IDBKeyRange.Companion.bound(
    x: Int,
    y: Int,
    lowerOpen: Boolean,
    upperOpen: Boolean,
): IDBKeyRange = bound(x.toJsNumber(), y.toJsNumber(), lowerOpen, upperOpen)

public fun IDBKeyRange.Companion.bound(
    x: Double,
    y: Double,
    lowerOpen: Boolean,
    upperOpen: Boolean,
): IDBKeyRange = bound(x.toJsNumber(), y.toJsNumber(), lowerOpen, upperOpen)

public fun IDBKeyRange.Companion.only(z: String?): IDBKeyRange = only(z?.toJsString())
public fun IDBKeyRange.Companion.only(z: Int): IDBKeyRange = only(z.toJsNumber())
public fun IDBKeyRange.Companion.only(z: Double): IDBKeyRange = only(z.toJsNumber())
