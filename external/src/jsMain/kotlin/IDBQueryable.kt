package com.juul.indexeddb.external

public actual external interface IDBQueryable {

    public actual fun count(): IDBRequest<JsNumber>
    public actual fun count(key: IDBKey?): IDBRequest<JsNumber>
    public actual fun count(key: IDBKeyRange): IDBRequest<JsNumber>

    public actual fun get(key: IDBKey): IDBRequest<*>
    public actual fun get(key: IDBKeyRange): IDBRequest<*>

    public actual fun getAll(): IDBRequest<ReadonlyArray<*>>
    public actual fun getAll(query: IDBKey?): IDBRequest<ReadonlyArray<*>>
    public actual fun getAll(query: IDBKey?, count: Int): IDBRequest<ReadonlyArray<*>>

    public actual fun getAll(query: IDBKeyRange?): IDBRequest<ReadonlyArray<*>>
    public actual fun getAll(query: IDBKeyRange?, count: Int): IDBRequest<ReadonlyArray<*>>

    public actual fun getAllKeys(): IDBRequest<ReadonlyArray<IDBKey>>
    public actual fun getAllKeys(query: IDBKey?): IDBRequest<ReadonlyArray<IDBKey>>
    public actual fun getAllKeys(query: IDBKey?, count: Int): IDBRequest<ReadonlyArray<IDBKey>>

    public actual fun getAllKeys(query: IDBKeyRange?): IDBRequest<ReadonlyArray<IDBKey>>
    public actual fun getAllKeys(query: IDBKeyRange?, count: Int): IDBRequest<ReadonlyArray<IDBKey>>

    public actual fun getKey(query: IDBKey): IDBRequest<IDBKey?>
    public actual fun getKey(query: IDBKeyRange): IDBRequest<IDBKey?>

    public actual fun openCursor(): IDBRequest<IDBCursorWithValue?>
    public actual fun openCursor(query: IDBKey?): IDBRequest<IDBCursorWithValue?>
    public actual fun openCursor(query: IDBKey?, direction: String): IDBRequest<IDBCursorWithValue?>

    public actual fun openCursor(query: IDBKeyRange?): IDBRequest<IDBCursorWithValue?>

    public actual fun openCursor(query: IDBKeyRange?, direction: String): IDBRequest<IDBCursorWithValue?>

    public actual fun openKeyCursor(): IDBRequest<IDBCursor?>
    public actual fun openKeyCursor(query: IDBKey?): IDBRequest<IDBCursor?>
    public actual fun openKeyCursor(query: IDBKey?, direction: String): IDBRequest<IDBCursor?>

    public actual fun openKeyCursor(query: IDBKeyRange?): IDBRequest<IDBCursor?>
    public actual fun openKeyCursor(query: IDBKeyRange?, direction: String): IDBRequest<IDBCursor?>
}
