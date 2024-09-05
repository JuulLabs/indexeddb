package com.juul.indexeddb.logs

public enum class Type {
    Database,
    Transaction,
    Query,
    // TODO: Cursor events would be nice, but they're slightly more complicated
}
