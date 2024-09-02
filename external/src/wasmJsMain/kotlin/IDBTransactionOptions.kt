package com.juul.indexeddb.external

public actual external interface IDBTransactionOptions : JsAny {
    public actual val durability: String
}

private fun IDBTransactionOptions(durability: String): IDBTransactionOptions =
    js("({ durability: durability })")

public actual fun IDBTransactionOptions(durability: IDBTransactionDurability): IDBTransactionOptions =
    IDBTransactionOptions(durability.value)
