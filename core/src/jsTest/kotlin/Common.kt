package com.juul.indexeddb

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.promise
import kotlin.js.Promise

@Suppress("EXPERIMENTAL_API_USAGE")
internal fun runTest(
    action: suspend CoroutineScope.() -> Unit
): Promise<Unit> = GlobalScope.promise { action.invoke(this) }
