package com.juul.indexeddb.logs

import org.w3c.dom.events.Event

public fun Logger.filterTypes(vararg whitelist: Type): Logger =
    filterTypes(whitelist.toSet())

public fun Logger.filterTypes(whitelist: Set<Type>): Logger =
    FilteringLogger(whitelist, this)

private class FilteringLogger(
    val whitelist: Set<Type>,
    val delegate: Logger,
) : Logger {

    override fun log(type: Type, event: Event?, message: () -> String) {
        if (type in whitelist) {
            delegate.log(type, event, message)
        }
    }
}
