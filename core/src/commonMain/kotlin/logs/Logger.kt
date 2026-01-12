package com.juul.indexeddb.logs

import com.juul.indexeddb.external.Event

public interface Logger {
    public fun log(type: Type, event: Event? = null, message: () -> String)
}
