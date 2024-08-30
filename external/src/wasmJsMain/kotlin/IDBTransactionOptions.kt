package com.juul.indexeddb.external

/** https://developer.mozilla.org/en-US/docs/Web/API/IDBTransaction/durability */
public external interface IDBTransactionOptions : JsAny {
    public val durability: JsString
}

private fun IDBTransactionOptions(durability: String): IDBTransactionOptions =
    js("({ durability: durability })")

public fun IDBTransactionOptions(durability: IDBTransactionDurability): IDBTransactionOptions =
    IDBTransactionOptions(durability.value)

/** https://developer.mozilla.org/en-US/docs/Web/API/IDBTransaction/durability */
public enum class IDBTransactionDurability(
    public val value: String,
) {
    Default("default"),
    Strict("strict"),
    Relaxed("relaxed"),
}
