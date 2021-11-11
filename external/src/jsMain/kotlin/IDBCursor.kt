package com.juul.indexeddb.external

/** https://developer.mozilla.org/en-US/docs/Web/API/IDBCursor */
public external interface IDBCursor {
    public val key: dynamic
    public val primaryKey: dynamic

    public fun `continue`()
}

/** https://developer.mozilla.org/en-US/docs/Web/API/IDBCursorWithValue */
public external interface IDBCursorWithValue : IDBCursor {
    public val value: dynamic

    public fun delete(): IDBRequest<dynamic>
    public fun update(value: dynamic): IDBRequest<dynamic>
}
