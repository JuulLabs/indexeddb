package com.juul.indexeddb.external

import kotlin.js.JsAny

public external interface IDBTransactionOptions : JsAny {
    public var durability: IDBDurability?
}
