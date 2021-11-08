package com.juul.indexeddb.external

/** https://developer.mozilla.org/en-US/docs/Web/API/IDBKeyRange */
public external class IDBKeyRange {

    public fun includes(value: dynamic): Boolean

    public companion object {
        public fun lowerBound(x: dynamic, open: Boolean): IDBKeyRange
        public fun upperBound(y: dynamic, open: Boolean): IDBKeyRange
        public fun bound(x: dynamic, y: dynamic, lowerOpen: Boolean, upperOpen: Boolean): IDBKeyRange
        public fun only(z: dynamic): IDBKeyRange
    }
}
