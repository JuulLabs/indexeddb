import com.juul.indexeddb.external.Event
import com.juul.indexeddb.external.EventTarget
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

/** Subscribes to events matching [type] and [moreTypes], unsubscribing immediately before [action] is called. */
internal suspend fun <T> EventTarget.onNextEvent(
    type: String,
    vararg moreTypes: String,
    action: (Event) -> T,
): T = suspendCancellableCoroutine { continuation ->
    lateinit var callback: (Event) -> Unit
    callback = { event ->
        removeEventListener(type, callback)
        moreTypes.forEach { type -> removeEventListener(type, callback) }
        try {
            continuation.resume(action.invoke(event))
        } catch (t: Throwable) {
            continuation.resumeWithException(EventHandlerException(t, event))
        }
    }
    addEventListener(type, callback)
    moreTypes.forEach { type -> addEventListener(type, callback) }
    continuation.invokeOnCancellation {
        removeEventListener(type, callback)
        moreTypes.forEach { type -> removeEventListener(type, callback) }
    }
}
