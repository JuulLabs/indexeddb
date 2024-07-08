package com.juul.indexeddb

import com.juul.indexeddb.external.IDBRequest

public class Request<T> internal constructor(
    internal val request: IDBRequest<T>,
)
