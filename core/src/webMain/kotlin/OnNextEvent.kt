package com.juul.indexeddb

import com.juul.indexeddb.external.Event
import com.juul.indexeddb.external.EventTarget
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

/** Subscribes to events matching [types], unsubscribing immediately before [action] is called. */
internal suspend fun <T> EventTarget.onNextEvent(
    vararg types: String,
    action: (Event) -> T,
): T = suspendCancellableCoroutine { continuation ->
    lateinit var callback: (Event) -> Unit
    callback = { event ->
        types.forEach { type -> removeEventListener(type, callback) }
        try {
            continuation.resume(action.invoke(event))
        } catch (t: Throwable) {
            continuation.resumeWithException(EventHandlerException(t, event))
        }
    }
    types.forEach { type -> addEventListener(type, callback) }
    continuation.invokeOnCancellation {
        types.forEach { type -> removeEventListener(type, callback) }
    }
}
