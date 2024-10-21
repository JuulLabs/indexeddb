package com.juul.indexeddb.external

public actual external interface IDBIndexOptions : JsAny {
    public actual var multiEntry: Boolean?
    public actual var unique: Boolean?
}

public actual fun IDBIndexOptions(
    block: IDBIndexOptions.() -> Unit,
): IDBIndexOptions = jso(block)
