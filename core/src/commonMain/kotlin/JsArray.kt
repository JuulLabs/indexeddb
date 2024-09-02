import com.juul.indexeddb.external.JsAny
import com.juul.indexeddb.external.JsArray
import com.juul.indexeddb.external.JsNumber
import com.juul.indexeddb.external.JsString
import com.juul.indexeddb.external.ReadonlyArray
import com.juul.indexeddb.external.set
import com.juul.indexeddb.external.toJsNumber
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

internal fun Array<out String>.toReadonlyArray(): ReadonlyArray<JsString> =
    JsArray<JsString>()
        .apply {
            forEachIndexed { index, s ->
                set(index, s.toJsString())
            }
        }.toReadonlyArray()

internal fun Array<out Int>.toReadonlyArray(): ReadonlyArray<JsNumber> =
    JsArray<JsNumber>()
        .apply {
            forEachIndexed { index, i ->
                set(index, i.toJsNumber())
            }
        }.toReadonlyArray()

internal fun Array<out Double>.toReadonlyArray(): ReadonlyArray<JsNumber> =
    JsArray<JsNumber>()
        .apply {
            forEachIndexed { index, d ->
                set(index, d.toJsNumber())
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
