package com.juul.indexeddb.logs

public enum class Type {
    CursorClose,
    CursorOpen,
    CursorValue,
    DatabaseClose,
    DatabaseDelete,
    DatabaseOpen,
    DatabaseUpgrade,
    QueryGet,
    QuerySet,
    TransactionClose,
    TransactionOpen,
}
