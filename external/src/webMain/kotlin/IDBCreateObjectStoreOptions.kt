package com.juul.indexeddb.external

import kotlin.js.JsAny

public external interface IDBCreateObjectStoreOptions : JsAny {
    public var autoIncrement: Boolean?
    public var keyPath: IDBKeyPath?
}
