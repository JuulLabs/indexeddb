package com.juul.indexeddb

import com.juul.indexeddb.external.IDBCreateObjectStoreOptions
import com.juul.indexeddb.external.IDBKeyPath
import kotlin.js.toJsArray
import kotlin.js.toJsString

public class KeyPath(
    private val path: String,
    private vararg val morePaths: String,
) {

    internal fun toOptions(): IDBCreateObjectStoreOptions = jso { keyPath = toJs() }
    internal fun toJs(): IDBKeyPath = when (morePaths.isEmpty()) {
        true -> path.toJsString()
        false -> arrayOf(path, *morePaths).map { it.toJsString() }.toJsArray()
    }
}
