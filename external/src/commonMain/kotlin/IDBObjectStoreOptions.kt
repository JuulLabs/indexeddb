package com.juul.indexeddb.external

/** https://developer.mozilla.org/en-US/docs/Web/API/IDBDatabase/createObjectStore#parameters */
public expect interface IDBObjectStoreOptions {
    public val autoIncrement: Boolean?
    public val keyPath: JsAny
}

public expect fun IDBObjectStoreOptions(
    autoIncrement: Boolean,
): IDBObjectStoreOptions

public expect fun IDBObjectStoreOptions(
    keyPath: JsAny?,
): IDBObjectStoreOptions
