package com.juul.indexeddb.external

import kotlin.js.JsAny

public external interface IDBCreateIndexOptions : JsAny {
    public var unique: Boolean?
    public var multiEntry: Boolean?

    /** If non-null, this should be either a locale code (such as `en-US` or `pl`), or `auto`. */
    public var locale: String?
}
