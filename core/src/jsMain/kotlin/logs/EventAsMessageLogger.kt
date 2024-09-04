package com.juul.indexeddb.logs

import com.juul.indexeddb.external.IDBVersionChangeEvent
import org.w3c.dom.events.Event

public fun Logger.embedEventsInMessages(
    separator: String = "\n    ",
): Logger = EventAsMessageLogger(separator, this)

private class EventAsMessageLogger(
    private val separator: String,
    private val delegate: Logger,
) : Logger {

    override fun log(type: Type, event: Event?, message: () -> String) {
        delegate.log(type, null) {
            buildString {
                append(message())
                if (event != null) {
                    for (line in propertyStrings(event)) {
                        append(separator)
                        append(line)
                    }
                }
            }
        }
    }

    private fun propertyStrings(event: Event): List<String> = buildList {
        add("type: ${event.type}")
        add("target: ${event.target}")
        if (event is IDBVersionChangeEvent) {
            add("oldVersion: ${event.oldVersion}")
            add("newVersion: ${event.newVersion}")
        }
    }
}
