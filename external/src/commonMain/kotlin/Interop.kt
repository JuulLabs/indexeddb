package com.juul.indexeddb.external

@PublishedApi
internal expect fun JsAny.unsafeCast(): IDBKey

@PublishedApi
internal expect fun Int.unsafeCast(): IDBKey

@PublishedApi
internal expect fun Double.unsafeCast(): IDBKey

@PublishedApi
internal expect fun String.unsafeCast(): IDBKey

@PublishedApi
internal expect fun Array<IDBKey>.unsafeCast(): IDBKey

public expect interface JsAny
public expect interface UnsafeJsAny : JsAny

public expect class JsArray<T : JsAny?>() : JsAny {
    public val length: Int
}

public expect operator fun <T : JsAny?> JsArray<T>.get(index: Int): T?

public expect operator fun <T : JsAny?> JsArray<T>.set(index: Int, value: T)

public expect class ReadonlyArray<T : JsAny?>() : JsAny {
    public val length: Int
}

public expect interface ArrayBuffer : JsAny
public expect interface ArrayBufferView : JsAny

public expect class JsNumber : JsAny
public expect fun JsNumber.toInt(): Int

public expect fun Int.toJsNumber(): JsNumber
public expect fun Double.toJsNumber(): JsNumber

public expect class JsBigInt : JsAny
public expect fun Long.toJsBigInt(): JsBigInt

public expect class JsString : JsAny

public expect fun String.toJsString(): JsString

public expect class JsBoolean : JsAny

public expect class JsByteArray : JsAny
public expect fun Array<Byte>.toJsByteArray(): JsByteArray

public expect class JsDate(
    value: JsString,
) : JsAny

public expect val definedExternally: Nothing

public expect open class Event {
    public open val type: String
    public open val target: EventTarget?
}

public expect abstract class EventTarget : JsAny {
    public fun addEventListener(type: String, callback: ((Event) -> Unit)?)
    public fun removeEventListener(type: String, callback: ((Event) -> Unit)?)
}

public expect abstract class Window
public expect val window: Window
public expect val Window.indexedDB: IDBFactory?
public expect val indexedDB: IDBFactory?
