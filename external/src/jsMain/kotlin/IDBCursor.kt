package com.juul.indexeddb.external

public actual external interface IDBCursor : JsAny {
    public actual val key: IDBKey
    public actual val primaryKey: IDBKey

    public actual fun advance(count: Int)

    public actual fun `continue`()
    public actual fun `continue`(key: IDBKey)

    public actual fun continuePrimaryKey(key: IDBKey, primaryKey: IDBKey)
}

public actual external interface IDBCursorWithValue : IDBCursor {
    public actual val value: JsAny?

    public actual fun delete(): IDBRequest<*>
    public actual fun update(value: JsAny?): IDBRequest<IDBKey>
}
