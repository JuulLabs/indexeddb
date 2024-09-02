@file:Suppress("NOTHING_TO_INLINE")

package com.juul.indexeddb.external

public expect sealed interface IDBKey : JsAny

public inline fun IDBKey(
    value: UnsafeJsAny,
): IDBKey = value.unsafeCast()

public inline fun IDBKey(
    value: JsNumber,
): IDBKey = value.unsafeCast()

public inline fun IDBKey(
    value: Int,
): IDBKey = value.unsafeCast()

public inline fun IDBKey(
    value: Double,
): IDBKey = value.unsafeCast()

public inline fun IDBKey(
    value: JsString,
): IDBKey = value.unsafeCast()

public inline fun IDBKey(
    value: String,
): IDBKey = value.unsafeCast()

public inline fun IDBKey(
    value: JsDate,
): IDBKey = value.unsafeCast()

public inline fun IDBKey(
    value: JsByteArray,
): IDBKey = value.unsafeCast()

public inline fun IDBKey(
    value: ArrayBuffer,
): IDBKey = value.unsafeCast()

public inline fun IDBKey(
    value: ArrayBufferView,
): IDBKey = value.unsafeCast()

public inline fun IDBKey(
    value: JsArray<IDBKey>,
): IDBKey = value.unsafeCast()

public inline fun IDBKey(
    value: Array<IDBKey>,
): IDBKey = value.unsafeCast()

public inline fun IDBKey(
    value: IDBKeyRange,
): IDBKey = value.unsafeCast()

public inline fun IDBKey(
    key: IDBKey,
    vararg moreKeys: IDBKey,
): IDBKey = JsArray<IDBKey>()
    .apply {
        set(0, key)
        for (i in moreKeys.indices) {
            set(i + 1, moreKeys[i])
        }
    }.unsafeCast()
