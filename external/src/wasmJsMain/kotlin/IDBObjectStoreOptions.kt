package com.juul.indexeddb.external

public actual external interface IDBObjectStoreOptions : JsAny {
    public actual val autoIncrement: Boolean?
    public actual val keyPath: JsAny
}

public actual fun IDBObjectStoreOptions(
    autoIncrement: Boolean,
): IDBObjectStoreOptions = js("({ autoIncrement: autoIncrement })")

public actual fun IDBObjectStoreOptions(
    keyPath: JsAny?,
): IDBObjectStoreOptions = js("({ keyPath: keyPath })")
