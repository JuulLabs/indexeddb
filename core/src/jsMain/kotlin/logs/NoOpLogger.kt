package com.juul.indexeddb.logs

import org.w3c.dom.events.Event

public object NoOpLogger : Logger {

    override fun log(type: Type, event: Event?, message: () -> String) {}
}
