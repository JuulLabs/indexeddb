package com.juul.indexeddb.external

/** https://developer.mozilla.org/en-US/docs/Web/API/IDBCursor */
public external interface IDBCursor : JsAny {
    public val key: IDBKey
    public val primaryKey: IDBKey

    public fun advance(count: Int)

    public fun `continue`(key: IDBKey = definedExternally)

    public fun continuePrimaryKey(key: IDBKey, primaryKey: IDBKey)
}

/** https://developer.mozilla.org/en-US/docs/Web/API/IDBCursorWithValue */
public external interface IDBCursorWithValue : IDBCursor {
    public val value: JsAny?

    public fun delete(): IDBRequest<*>
    public fun update(value: JsAny?): IDBRequest<IDBKey>
}
