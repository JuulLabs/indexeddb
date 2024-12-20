package com.juul.indexeddb.logs

import com.juul.khronicle.Log
import com.juul.khronicle.LogLevel
import org.w3c.dom.events.Event as JsEvent

public object KhronicleLogger : Logger {

    override fun log(type: Type, event: JsEvent?, message: () -> String) {
        val level = when (event?.type) {
            "error", "blocked" -> LogLevel.Error
            else -> when (type) {
                Type.Database -> LogLevel.Info
                Type.Transaction -> LogLevel.Debug
                else -> LogLevel.Verbose
            }
        }
        Log.dynamic(level = level, tag = "IndexedDB/$type") { metadata ->
            if (event != null) {
                metadata[Event] = event
            }
            message()
        }
    }
}
