import com.juul.indexeddb.external.IDBCursor
import com.juul.indexeddb.external.IDBCursorWithValue
import com.juul.indexeddb.external.IDBKey
import com.juul.indexeddb.external.JsNumber
import com.juul.indexeddb.external.ReadonlyArray

public sealed class Queryable {
    internal abstract fun requestGet(key: IDBKey): Request<*>
    internal abstract fun requestGetAll(query: IDBKey?): Request<ReadonlyArray<*>>
    internal abstract fun requestOpenCursor(query: IDBKey?, direction: Cursor.Direction): Request<IDBCursorWithValue?>
    internal abstract fun requestOpenKeyCursor(query: IDBKey?, direction: Cursor.Direction): Request<IDBCursor?>
    internal abstract fun requestCount(query: IDBKey?): Request<JsNumber>
}
