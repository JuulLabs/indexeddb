package com.juul.indexeddb

import com.juul.indexeddb.Durability.Default
import com.juul.indexeddb.Durability.Relaxed
import com.juul.indexeddb.Durability.Strict

/** https://developer.mozilla.org/en-US/docs/Web/API/IDBTransaction/durability */
public enum class Durability {
    Default,
    Strict,
    Relaxed,
}

internal val Durability.jsValue: String
    get() = when (this) {
        Default -> "default"
        Strict -> "strict"
        Relaxed -> "relaxed"
    }
