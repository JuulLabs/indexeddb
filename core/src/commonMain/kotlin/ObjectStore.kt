import com.juul.indexeddb.external.IDBCursor
import com.juul.indexeddb.external.IDBCursorWithValue
import com.juul.indexeddb.external.IDBKey
import com.juul.indexeddb.external.IDBObjectStore
import com.juul.indexeddb.external.JsNumber
import com.juul.indexeddb.external.ReadonlyArray

public class ObjectStore internal constructor(
    internal val objectStore: IDBObjectStore,
) : Queryable() {
    override fun requestGet(key: IDBKey): Request<*> =
        Request(objectStore.get(key))

    override fun requestGetAll(query: IDBKey?): Request<ReadonlyArray<*>> =
        Request(objectStore.getAll(query))

    override fun requestOpenCursor(query: IDBKey?, direction: Cursor.Direction): Request<IDBCursorWithValue?> =
        Request(objectStore.openCursor(query, direction.constant))

    override fun requestOpenKeyCursor(query: IDBKey?, direction: Cursor.Direction): Request<IDBCursor?> =
        Request(objectStore.openKeyCursor(query, direction.constant))

    override fun requestCount(query: IDBKey?): Request<JsNumber> =
        Request(objectStore.count(query))
}
