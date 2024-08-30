package com.juul.indexeddb.external

/** https://developer.mozilla.org/en-US/docs/Web/API/IDBDatabase/createObjectStore#parameters */
public external interface IDBObjectStoreOptions : JsAny {
    public val autoIncrement: Boolean?
    public val keyPath: JsAny
}

public fun IDBObjectStoreOptions(
    autoIncrement: Boolean,
): IDBObjectStoreOptions =
    js("({ autoIncrement: autoIncrement })")

public fun IDBObjectStoreOptions(
    autoIncrement: Boolean,
    keyPath: JsAny?,
): IDBObjectStoreOptions = js("({ autoIncrement: autoIncrement, keyPath: keyPath })")
