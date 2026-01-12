package com.juul.indexeddb.external

import kotlin.js.JsAny

/** https://developer.mozilla.org/en-US/docs/Web/API/IDBKeyRange */
public external class IDBKeyRange : JsAny {

    public val lower: IDBKey?
    public val upper: IDBKey?
    public val lowerOpen: Boolean
    public val upperOpen: Boolean
    public fun includes(key: IDBKey): Boolean

    public companion object {
        public fun lowerBound(x: IDBKey, open: Boolean): IDBKeyRange
        public fun upperBound(y: IDBKey, open: Boolean): IDBKeyRange
        public fun bound(x: IDBKey, y: IDBKey, lowerOpen: Boolean, upperOpen: Boolean): IDBKeyRange
        public fun only(z: IDBKey): IDBKeyRange
    }
}
