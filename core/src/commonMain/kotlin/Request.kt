package com.juul.indexeddb

import com.juul.indexeddb.external.IDBRequest
import kotlin.js.JsAny

public class Request<T : JsAny?> internal constructor(
    internal val request: IDBRequest<T>,
)
