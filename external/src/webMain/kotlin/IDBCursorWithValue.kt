package com.juul.indexeddb.external

/** https://developer.mozilla.org/en-US/docs/Web/API/IDBCursorWithValue */
public external interface IDBCursorWithValue : IDBCursor {
    public val value: IDBValue

    public fun delete(): IDBRequest<Nothing?>
    public fun update(value: IDBValue): IDBRequest<IDBKey>
}
