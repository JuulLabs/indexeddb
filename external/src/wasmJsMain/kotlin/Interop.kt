package com.juul.indexeddb.external

import kotlinx.browser.window
import org.khronos.webgl.Uint8Array
import kotlin.js.get as wasmJsGet
import kotlin.js.set as wasmJsSet
import kotlin.js.toInt as jsToInt
import kotlin.js.toJsBigInt as jsToJsBigInt
import kotlin.js.toJsNumber as jsToJsNumber
import kotlin.js.toJsString as jsToJsString
import kotlin.js.unsafeCast as jsUnsafeCast

@PublishedApi
internal actual fun JsAny.unsafeCast(): IDBKey = jsUnsafeCast()

@PublishedApi
internal actual fun Int.unsafeCast(): IDBKey = toJsNumber().jsUnsafeCast()

@PublishedApi
internal actual fun Double.unsafeCast(): IDBKey = toJsNumber().jsUnsafeCast()

@PublishedApi
internal actual fun String.unsafeCast(): IDBKey = toJsString().jsUnsafeCast()

@PublishedApi
internal actual fun Array<IDBKey>.unsafeCast(): IDBKey =
    JsArray<IDBKey>()
        .also { ja ->
            for (i in indices) {
                ja[i] = this[i]
            }
        }.jsUnsafeCast()

internal actual typealias JsAny = kotlin.js.JsAny

public actual external interface UnsafeJsAny : JsAny

public actual typealias JsArray<T> = kotlin.js.JsArray<T>

public actual typealias ReadonlyArray<T> = kotlin.js.JsArray<T>

public actual operator fun <T : JsAny?> JsArray<T>.get(index: Int): T? = this.wasmJsGet(index)

public actual operator fun <T : JsAny?> JsArray<T>.set(index: Int, value: T) {
    this.wasmJsSet(index, value)
}

@JsName("ArrayBuffer")
public actual external interface ArrayBuffer : JsAny

@JsName("ArrayBufferView")
public actual external interface ArrayBufferView : JsAny

public actual typealias JsBoolean = kotlin.js.JsBoolean

public actual typealias JsNumber = kotlin.js.JsNumber

public actual fun JsNumber.toInt(): Int = jsToInt()

public actual fun Int.toJsNumber(): JsNumber = jsToJsNumber()
public actual fun Double.toJsNumber(): JsNumber = jsToJsNumber()

public actual typealias JsBigInt = kotlin.js.JsBigInt

public actual fun Long.toJsBigInt(): JsBigInt = jsToJsBigInt()

public actual typealias JsString = kotlin.js.JsString

public actual fun String.toJsString(): JsString = jsToJsString()

public actual typealias JsByteArray = Uint8Array

public actual fun Array<Byte>.toJsByteArray(): JsByteArray = Uint8Array(
    JsArray<JsNumber>().also { ja ->
        for (i in indices) {
            ja[i] = this[i].toInt().toJsNumber()
        }
    },
)

@JsName("Date")
public actual external class JsDate actual constructor(
    value: JsString,
) : JsAny

@Suppress("WRONG_JS_INTEROP_TYPE")
public actual external val definedExternally: Nothing = kotlin.js.definedExternally

public actual open external class Event {
    public actual open val type: String
    public actual open val target: EventTarget?
}

public actual abstract external class EventTarget : JsAny {
    public actual fun addEventListener(type: String, callback: ((Event) -> Unit)?)
    public actual fun removeEventListener(type: String, callback: ((Event) -> Unit)?)
}

public actual typealias Window = org.w3c.dom.Window

public actual val window: Window = window
public actual val Window.indexedDB: IDBFactory? get() = com.juul.indexeddb.external.indexedDB
public actual val indexedDB: IDBFactory? = js("window.indexedDB")
