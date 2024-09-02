package com.juul.indexeddb.external

/** https://developer.mozilla.org/en-US/docs/Web/API/IDBTransaction/durability */
public expect interface IDBTransactionOptions {
    public val durability: String
}

public enum class IDBTransactionDurability(
    public val value: String,
) {
    Default("default"),
    Strict("strict"),
    Relaxed("relaxed"),
}

public expect fun IDBTransactionOptions(durability: IDBTransactionDurability): IDBTransactionOptions
