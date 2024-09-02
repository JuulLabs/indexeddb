import com.juul.indexeddb.external.IDBCursor
import com.juul.indexeddb.external.IDBCursorWithValue
import com.juul.indexeddb.external.IDBKey
import com.juul.indexeddb.external.JsAny
import kotlinx.coroutines.channels.SendChannel

public open class Cursor internal constructor(
    internal open val cursor: IDBCursor,
    private val channel: SendChannel<*>,
) {
    public val key: IDBKey
        get() = cursor.key

    public val primaryKey: IDBKey
        get() = cursor.primaryKey

    public fun close() {
        channel.close()
    }

    public fun `continue`() {
        cursor.`continue`()
    }

    public fun advance(count: Int) {
        cursor.advance(count)
    }

    public fun `continue`(key: IDBKey) {
        cursor.`continue`(key)
    }

    public fun continuePrimaryKey(key: IDBKey, primaryKey: IDBKey) {
        cursor.continuePrimaryKey(key, primaryKey)
    }

    public enum class Direction(
        internal val constant: String,
    ) {
        Next("next"),
        NextUnique("nextunique"),
        Previous("prev"),
        PreviousUnique("prevunique"),
    }
}

public class CursorWithValue internal constructor(
    override val cursor: IDBCursorWithValue,
    channel: SendChannel<*>,
) : Cursor(cursor, channel) {
    public val value: JsAny?
        get() = cursor.value
}
