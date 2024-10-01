import com.juul.indexeddb.external.IDBCursor
import com.juul.indexeddb.external.IDBCursorWithValue
import com.juul.indexeddb.external.IDBIndex
import com.juul.indexeddb.external.IDBKey
import com.juul.indexeddb.external.JsNumber
import com.juul.indexeddb.external.ReadonlyArray

public class Index internal constructor(
    internal val index: IDBIndex,
) : Queryable() {
    override fun requestGet(key: IDBKey): Request<*> =
        Request(index.get(key))

    override fun requestGetAll(query: IDBKey?): Request<ReadonlyArray<*>> =
        Request(index.getAll(query))

    override fun requestOpenCursor(
        query: IDBKey?,
        direction: Cursor.Direction,
    ): Request<IDBCursorWithValue?> =
        Request(index.openCursor(query, direction.constant))

    override fun requestOpenKeyCursor(
        query: IDBKey?,
        direction: Cursor.Direction,
    ): Request<IDBCursor?> =
        Request(index.openKeyCursor(query, direction.constant))

    override fun requestCount(query: IDBKey?): Request<JsNumber> =
        Request(index.count(query))
}
