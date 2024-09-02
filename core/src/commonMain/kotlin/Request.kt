import com.juul.indexeddb.external.IDBRequest
import com.juul.indexeddb.external.JsAny

internal class Request<T : JsAny?> internal constructor(
    internal val request: IDBRequest<T>,
)
