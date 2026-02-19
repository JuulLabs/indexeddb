package com.juul.indexeddb.external

import kotlin.js.JsAny

/** https://developer.mozilla.org/en-US/docs/Web/API/IDBCursor */
public external interface IDBCursor : JsAny {
    public val key: IDBKey
    public val primaryKey: IDBKey

    public fun advance(count: Int)

    public fun `continue`()

    public fun `continue`(key: IDBKey)

    public fun continuePrimaryKey(key: IDBKey, primaryKey: IDBKey)
}
