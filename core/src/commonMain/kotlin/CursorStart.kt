import com.juul.indexeddb.external.IDBCursor
import com.juul.indexeddb.external.IDBKey

public sealed class CursorStart {

    internal abstract fun apply(cursor: IDBCursor)

    public data class Advance(
        val count: Int,
    ) : CursorStart() {
        override fun apply(cursor: IDBCursor) {
            cursor.advance(count)
        }
    }

    public data class Continue(
        val key: IDBKey,
    ) : CursorStart() {
        override fun apply(cursor: IDBCursor) {
            cursor.`continue`(key)
        }
    }

    public data class ContinuePrimaryKey(
        val key: IDBKey,
        val primaryKey: IDBKey,
    ) : CursorStart() {
        override fun apply(cursor: IDBCursor) {
            cursor.continuePrimaryKey(key, primaryKey)
        }
    }
}
