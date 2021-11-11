package com.juul.indexeddb

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.promise
import kotlin.js.Promise
import kotlin.test.AfterTest

/**
 * Runs a coroutine in a promise, which the Mocha runner is smart enough to block on.
 *
 * The [action] runs inside a [TestScope], which gives access to [TestScope.onCleanup]. This
 * allows for test-specific [AfterTest] like behavior with access to `suspend` functions.
 */
@Suppress("EXPERIMENTAL_API_USAGE")
internal fun runTest(
    action: suspend TestScope.() -> Unit,
): Promise<Unit> = GlobalScope.promise {
    val testScope = TestScope(this)
    try {
        action.invoke(testScope)
    } finally {
        testScope.cleanup()
    }
}

internal class TestScope(
    inner: CoroutineScope,
) : CoroutineScope by inner {

    private val callbacks = mutableListOf<suspend () -> Unit>()

    fun onCleanup(action: suspend () -> Unit) {
        callbacks += action
    }

    suspend fun cleanup() {
        callbacks.asReversed().forEach { it.invoke() }
    }
}
