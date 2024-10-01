package com.juul.indexeddb.external

public actual external class IDBKeyRange : JsAny {

    public actual fun includes(value: JsAny?): Boolean

    public actual companion object {
        public actual fun lowerBound(x: JsAny?, open: Boolean): IDBKeyRange
        public actual fun upperBound(y: JsAny?, open: Boolean): IDBKeyRange
        public actual fun bound(x: JsAny?, y: JsAny?, lowerOpen: Boolean, upperOpen: Boolean): IDBKeyRange
        public actual fun only(z: JsAny?): IDBKeyRange
    }
}
