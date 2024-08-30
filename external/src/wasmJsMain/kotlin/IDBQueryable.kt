package com.juul.indexeddb.external

/** Pseudo-interface for the shared query functionality between [IDBIndex] and [IDBObjectStore]. */
public external interface IDBQueryable : JsAny {

    public fun count(key: IDBKey? = definedExternally): IDBRequest<JsNumber>
    public fun count(key: IDBKeyRange): IDBRequest<JsNumber>

    public fun get(key: IDBKey): IDBRequest<*>
    public fun get(key: IDBKeyRange): IDBRequest<*>

    public fun getAll(
        query: IDBKey? = definedExternally,
        count: Int = definedExternally,
    ): IDBRequest<ReadonlyArray<*>>

    public fun getAll(
        query: IDBKeyRange?,
        count: Int = definedExternally,
    ): IDBRequest<ReadonlyArray<*>>

    public fun getAllKeys(
        query: IDBKey? = definedExternally,
        count: Int = definedExternally,
    ): IDBRequest<ReadonlyArray<IDBKey>>

    public fun getAllKeys(
        query: IDBKeyRange?,
        count: Int = definedExternally,
    ): IDBRequest<ReadonlyArray<IDBKey>>

    public fun getKey(query: IDBKey): IDBRequest<IDBKey?>
    public fun getKey(query: IDBKeyRange): IDBRequest<IDBKey?>

    public fun openCursor(
        query: IDBKey? = definedExternally,
        direction: String = definedExternally,
    ): IDBRequest<IDBCursorWithValue?>

    public fun openCursor(
        query: IDBKeyRange?,
        direction: String = definedExternally,
    ): IDBRequest<IDBCursorWithValue?>

    public fun openKeyCursor(
        query: IDBKey? = definedExternally,
        direction: String = definedExternally,
    ): IDBRequest<IDBCursor?>

    public fun openKeyCursor(
        query: IDBKeyRange?,
        direction: String = definedExternally,
    ): IDBRequest<IDBCursor?>
}
