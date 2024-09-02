import com.juul.indexeddb.external.IDBKey
import com.juul.indexeddb.external.IDBKeyRange
import com.juul.indexeddb.external.JsDate
import com.juul.indexeddb.external.toJsString
import com.juul.indexeddb.external.upperBound
import com.juul.indexeddb.toJsByteArray
import kotlin.test.Test

public class KeyTests {

    @Test
    public fun constructor_withString_completes() {
        IDBKey("string")
    }

    @Test
    public fun constructor_withDate_completes() {
        IDBKey(JsDate("2021-11-11T12:00:00".toJsString()))
    }

    @Test
    public fun constructor_withNiceNumbers_completes() {
        IDBKey(IDBKey(1), IDBKey(3.0))
    }

    @Test
    public fun constructor_withByteArray_completes() {
        IDBKey(byteArrayOf(1, 2, 3, 4, 5, 6).toJsByteArray())
    }

    @Test
    public fun constructor_withArrayOfString_completes() {
        IDBKey(JsArray(IDBKey(JsArray(IDBKey("foo"), IDBKey("bar")))))
    }

    @Test
    public fun constructor_withRange_completes() {
        IDBKey(IDBKeyRange.upperBound("foobar", false))
    }
}
