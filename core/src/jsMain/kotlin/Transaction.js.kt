import com.juul.indexeddb.external.JsAny

/**
 * Adds a new item to the database using an in-line or auto-incrementing key. If an item with the same
 * key already exists, this will fail.
 *
 * This API is delicate. If you're passing in Kotlin objects directly, you're probably doing it wrong.
 *
 * Generally, you'll want to create an explicit `external interface` and pass that in, to guarantee that Kotlin
 * doesn't mangle, prefix, or otherwise mess with your field names.
 */
public suspend fun WriteTransaction.add(objectStore: ObjectStore, item: dynamic): JsAny =
    // TODO: this would benefit from context parameters
    objectStore.add(item.unsafeCast<JsAny?>())
