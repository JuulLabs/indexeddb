package com.juul.indexeddb.external

import kotlin.js.JsAny

/** https://developer.mozilla.org/en-US/docs/Web/API/IDBRequest */
public external interface IDBRequest<T : JsAny?> : EventTarget {
    public val error: DOMException?
    public val transaction: IDBTransaction?
    public val result: T
    public var onerror: (Event) -> Unit
    public var onsuccess: (Event) -> Unit
}
