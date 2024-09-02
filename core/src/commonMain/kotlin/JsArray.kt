import com.juul.indexeddb.external.JsAny
import com.juul.indexeddb.external.JsArray
import com.juul.indexeddb.external.JsString
import com.juul.indexeddb.external.ReadonlyArray
import com.juul.indexeddb.external.set
import com.juul.indexeddb.external.toJsString
import com.juul.indexeddb.toReadonlyArray

internal fun toReadonlyArray(value: String, vararg moreValues: String): ReadonlyArray<JsString> =
    JsArray<JsString>()
        .apply {
            set(0, value.toJsString())
            moreValues.forEachIndexed { index, s ->
                set(index + 1, s.toJsString())
            }
        }.toReadonlyArray()

internal fun Iterable<String?>.toJsArray(): JsArray<JsString?> =
    JsArray<JsString?>().apply {
        forEachIndexed { index, s ->
            set(index, s?.toJsString())
        }
    }

internal fun <T : JsAny?> JsArray(vararg values: T): JsArray<T> =
    JsArray<T>().apply {
        for (i in values.indices) {
            set(i, values[i])
        }
    }
