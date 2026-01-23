package com.juul.indexeddb

import com.juul.indexeddb.external.IDBCreateObjectStoreOptions

public object AutoIncrement {
    internal fun toOptions(): IDBCreateObjectStoreOptions = jso { autoIncrement = true }
}
