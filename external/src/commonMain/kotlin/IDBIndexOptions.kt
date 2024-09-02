package com.juul.indexeddb.external

public expect interface IDBIndexOptions {
    public var multiEntry: Boolean?
    public var unique: Boolean?
}

public expect fun IDBIndexOptions(block: IDBIndexOptions.() -> Unit): IDBIndexOptions
