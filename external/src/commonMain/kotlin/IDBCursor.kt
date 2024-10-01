package com.juul.indexeddb.external

/** https://developer.mozilla.org/en-US/docs/Web/API/IDBCursor */
public expect interface IDBCursor : JsAny {
    public val key: IDBKey
    public val primaryKey: IDBKey

    public fun advance(count: Int)

    public fun `continue`()
    public fun `continue`(key: IDBKey)

    public fun continuePrimaryKey(key: IDBKey, primaryKey: IDBKey)
}

/** https://developer.mozilla.org/en-US/docs/Web/API/IDBCursorWithValue */
public expect interface IDBCursorWithValue : IDBCursor {
    public val value: JsAny?

    public fun delete(): IDBRequest<*>
    public fun update(value: JsAny?): IDBRequest<IDBKey>
}
