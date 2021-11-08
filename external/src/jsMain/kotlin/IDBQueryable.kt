package com.juul.indexeddb.external

/** Pseudo-interface for the shared query functionality between [IDBIndex] and [IDBObjectStore]. */
public external interface IDBQueryable {

    public fun count(): IDBRequest<Int>
    public fun count(key: dynamic): IDBRequest<Int>

    public fun get(key: dynamic): IDBRequest<Int>

    public fun getAll(): IDBRequest<dynamic>
    public fun getAll(query: dynamic): IDBRequest<dynamic>
    public fun getAll(query: dynamic, count: Int): IDBRequest<dynamic>

    public fun getAllKeys(): IDBRequest<dynamic>
    public fun getAllKeys(query: dynamic): IDBRequest<dynamic>
    public fun getAllKeys(query: dynamic, count: Int): IDBRequest<dynamic>

    public fun getKey(key: dynamic): IDBRequest<dynamic>

    public fun openCursor(): IDBRequest<IDBCursorWithValue?>
    public fun openCursor(query: dynamic): IDBRequest<IDBCursorWithValue?>
    public fun openCursor(query: dynamic, direction: String): IDBRequest<IDBCursorWithValue?>

    public fun openKeyCursor(): IDBRequest<IDBCursor?>
    public fun openKeyCursor(query: dynamic): IDBRequest<IDBCursor?>
    public fun openKeyCursor(query: dynamic, direction: String): IDBRequest<IDBCursor?>
}
