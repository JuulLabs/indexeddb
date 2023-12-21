package com.juul.indexeddb

/** https://developer.mozilla.org/en-US/docs/Web/API/IDBTransaction/durability */
public enum class Durability(
    internal val jsValue: String
) {
    Default("default"),
    Strict("strict"),
    Relaxed("relaxed");
}
