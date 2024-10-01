package com.juul.indexeddb.logs

import org.w3c.dom.events.Event

public interface Logger {
    public fun log(type: Type, event: Event? = null, message: () -> String)
}
