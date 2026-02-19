package com.juul.indexeddb.external

import kotlin.js.JsAny
import kotlin.js.JsArray
import kotlin.js.JsNumber

/** Pseudo-interface for the shared query functionality between [IDBIndex] and [IDBObjectStore]. */
public external interface IDBQueryable : JsAny {

    public fun count(): IDBRequest<JsNumber>
    public fun count(key: IDBKey): IDBRequest<JsNumber>

    public fun get(key: IDBKey): IDBRequest<IDBValue>

    public fun getAll(): IDBRequest<JsArray<IDBValue>>
    public fun getAll(query: IDBKey): IDBRequest<JsArray<IDBValue>>
    public fun getAll(query: IDBKey, count: Int): IDBRequest<JsArray<IDBValue>>

    public fun getAllKeys(): IDBRequest<JsArray<IDBKey>>
    public fun getAllKeys(query: IDBKey): IDBRequest<JsArray<IDBKey>>
    public fun getAllKeys(query: IDBKey, count: Int): IDBRequest<JsArray<IDBKey>>

    public fun getKey(key: IDBKey): IDBRequest<IDBKey>

    public fun openCursor(): IDBRequest<IDBCursorWithValue?>
    public fun openCursor(query: IDBKey): IDBRequest<IDBCursorWithValue?>
    public fun openCursor(query: IDBKey, direction: IDBCursorDirection): IDBRequest<IDBCursorWithValue?>

    public fun openKeyCursor(): IDBRequest<IDBCursor?>
    public fun openKeyCursor(query: IDBKey): IDBRequest<IDBCursor?>
    public fun openKeyCursor(query: IDBKey, direction: IDBCursorDirection): IDBRequest<IDBCursor?>
}
