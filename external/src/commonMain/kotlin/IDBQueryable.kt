package com.juul.indexeddb.external

/** Pseudo-interface for the shared query functionality between [IDBIndex] and [IDBObjectStore]. */
public expect interface IDBQueryable {

    public fun count(): IDBRequest<JsNumber>
    public fun count(key: IDBKey?): IDBRequest<JsNumber>
    public fun count(key: IDBKeyRange): IDBRequest<JsNumber>

    public fun get(key: IDBKey): IDBRequest<*>
    public fun get(key: IDBKeyRange): IDBRequest<*>

    public fun getAll(): IDBRequest<ReadonlyArray<*>>
    public fun getAll(query: IDBKey?): IDBRequest<ReadonlyArray<*>>
    public fun getAll(query: IDBKey?, count: Int): IDBRequest<ReadonlyArray<*>>

    public fun getAll(query: IDBKeyRange?): IDBRequest<ReadonlyArray<*>>
    public fun getAll(query: IDBKeyRange?, count: Int): IDBRequest<ReadonlyArray<*>>

    public fun getAllKeys(): IDBRequest<ReadonlyArray<IDBKey>>
    public fun getAllKeys(query: IDBKey?): IDBRequest<ReadonlyArray<IDBKey>>
    public fun getAllKeys(query: IDBKey?, count: Int): IDBRequest<ReadonlyArray<IDBKey>>

    public fun getAllKeys(query: IDBKeyRange?): IDBRequest<ReadonlyArray<IDBKey>>
    public fun getAllKeys(query: IDBKeyRange?, count: Int): IDBRequest<ReadonlyArray<IDBKey>>

    public fun getKey(query: IDBKey): IDBRequest<IDBKey?>
    public fun getKey(query: IDBKeyRange): IDBRequest<IDBKey?>

    public fun openCursor(): IDBRequest<IDBCursorWithValue?>
    public fun openCursor(query: IDBKey?): IDBRequest<IDBCursorWithValue?>
    public fun openCursor(query: IDBKey?, direction: String): IDBRequest<IDBCursorWithValue?>

    public fun openCursor(query: IDBKeyRange?): IDBRequest<IDBCursorWithValue?>
    public fun openCursor(query: IDBKeyRange?, direction: String): IDBRequest<IDBCursorWithValue?>

    public fun openKeyCursor(): IDBRequest<IDBCursor?>
    public fun openKeyCursor(query: IDBKey?): IDBRequest<IDBCursor?>
    public fun openKeyCursor(query: IDBKey?, direction: String): IDBRequest<IDBCursor?>

    public fun openKeyCursor(query: IDBKeyRange?): IDBRequest<IDBCursor?>
    public fun openKeyCursor(query: IDBKeyRange?, direction: String): IDBRequest<IDBCursor?>
}
