package com.juul.indexeddb.external

import kotlinx.browser.window
import org.khronos.webgl.Uint8Array
import kotlin.js.unsafeCast
import kotlin.js.unsafeCast as jsUnsafeCast

@PublishedApi
internal actual fun JsAny.unsafeCast(): IDBKey = jsUnsafeCast<IDBKey>()

@PublishedApi
internal actual fun Int.unsafeCast(): IDBKey = jsUnsafeCast<IDBKey>()

@PublishedApi
internal actual fun Double.unsafeCast(): IDBKey = jsUnsafeCast<IDBKey>()

@PublishedApi
internal actual fun String.unsafeCast(): IDBKey = jsUnsafeCast<IDBKey>()

@PublishedApi
internal actual fun Array<IDBKey>.unsafeCast(): IDBKey = jsUnsafeCast<IDBKey>()

public actual external interface JsAny
public actual external interface UnsafeJsAny : JsAny

@JsName("Array")
public actual external class JsArray<T : JsAny?> actual constructor() : JsAny {
    public actual val length: Int
}

@JsName("Array")
public actual external class ReadonlyArray<T : JsAny?> actual constructor() : JsAny {
    public actual val length: Int
}

public actual operator fun <T : JsAny?> JsArray<T>.get(index: Int): T? =
    jsArrayGet(this, index)

public actual operator fun <T : JsAny?> JsArray<T>.set(index: Int, value: T) {
    jsArraySet(this, index, value)
}

@Suppress("UnsafeCastFromDynamic")
private fun <T : JsAny?> jsArrayGet(array: JsArray<T>, index: Int): T? =
    js("array[index]")

private fun <T : JsAny?> jsArraySet(array: JsArray<T>, index: Int, value: T) {
    js("array[index] = value")
}

@JsName("ArrayBuffer")
public actual external interface ArrayBuffer : JsAny

@JsName("ArrayBufferView")
public actual external interface ArrayBufferView : JsAny

@JsName("Boolean")
public actual external class JsBoolean : JsAny

@JsName("Number")
public actual external class JsNumber : JsAny

public actual fun JsNumber.toInt(): Int = this.unsafeCast<Number>().toInt()

public actual fun Int.toJsNumber(): JsNumber = unsafeCast<JsNumber>()
public actual fun Double.toJsNumber(): JsNumber = unsafeCast<JsNumber>()

@JsName("BigInt")
public actual external class JsBigInt : JsAny

public actual fun Long.toJsBigInt(): JsBigInt = unsafeCast<JsBigInt>()

@JsName("String")
public actual external class JsString : JsAny

public actual fun String.toJsString(): JsString = this.jsUnsafeCast<JsString>()

@JsName("Uint8Array")
public actual external class JsByteArray :
    Uint8Array,
    JsAny

public actual fun Array<Byte>.toJsByteArray(): JsByteArray = Uint8Array(this).unsafeCast<JsByteArray>()

@JsName("Date")
public actual external class JsDate actual constructor(
    value: JsString,
) : JsAny

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
public actual val indexedDB: IDBFactory? = js("window.indexedDB").unsafeCast<IDBFactory?>()
