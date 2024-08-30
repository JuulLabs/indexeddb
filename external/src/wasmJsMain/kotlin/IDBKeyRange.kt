package com.juul.indexeddb.external

/** https://developer.mozilla.org/en-US/docs/Web/API/IDBKeyRange */
public external class IDBKeyRange : JsAny {

    public fun includes(value: JsAny?): Boolean

    public companion object {
        public fun lowerBound(x: JsAny?, open: Boolean = definedExternally): IDBKeyRange

        public fun upperBound(y: JsAny?, open: Boolean = definedExternally): IDBKeyRange

        public fun bound(
            x: JsAny?,
            y: JsAny?,
            lowerOpen: Boolean = definedExternally,
            upperOpen: Boolean = definedExternally,
        ): IDBKeyRange

        public fun only(z: JsAny?): IDBKeyRange
    }
}
