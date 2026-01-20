package com.juul.indexeddb.logs

import com.juul.indexeddb.external.Event

public object PrintLogger : Logger {

    override fun log(type: Type, event: Event?, message: () -> String) {
        val msg = message()
        when (event) {
            null -> println("$type: $msg")
            else -> println("$type (event=${event.type}): $msg")
        }
    }
}
