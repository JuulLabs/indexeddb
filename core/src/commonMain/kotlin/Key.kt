import com.juul.indexeddb.external.IDBKey
import com.juul.indexeddb.external.IDBKeyRange
import com.juul.indexeddb.external.JsAny
import com.juul.indexeddb.external.toJsString

public object AutoIncrement

public class KeyPath private constructor(
    private val paths: List<String?>,
) {
    init {
        require(paths.isNotEmpty()) { "A key path must have at least one member." }
    }

    public constructor(path: String?, vararg morePaths: String?) : this(listOf(path, *morePaths))

    internal fun toJs(): JsAny? = if (paths.size == 1) paths[0]?.toJsString() else paths.toJsArray()
}

public fun lowerBound(
    x: JsAny?,
    open: Boolean = false,
): IDBKey = IDBKey(IDBKeyRange.lowerBound(x, open))

public fun upperBound(
    y: JsAny?,
    open: Boolean = false,
): IDBKey = IDBKey(IDBKeyRange.upperBound(y, open))

public fun bound(
    x: JsAny?,
    y: JsAny?,
    lowerOpen: Boolean = false,
    upperOpen: Boolean = false,
): IDBKey = IDBKey(IDBKeyRange.bound(x, y, lowerOpen, upperOpen))

public fun only(
    z: JsAny?,
): IDBKey = IDBKey(IDBKeyRange.only(z))
