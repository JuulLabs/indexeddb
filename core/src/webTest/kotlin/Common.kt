package com.juul.indexeddb

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.test.TestResult

/**
 * Wrapper around [kotlinx.coroutines.test.runTest] that allows for tests to add cleanup actions
 * in code during test execution.
 */
@Suppress("EXPERIMENTAL_API_USAGE")
internal fun runTest(
    action: suspend TestScope.() -> Unit,
): TestResult = kotlinx.coroutines.test.runTest {
    val testScope = TestScope(this)
    try {
        action.invoke(testScope)
    } finally {
        testScope.cleanup()
    }
}

internal class TestScope(
    delegate: CoroutineScope,
) : CoroutineScope by delegate {

    private val callbacks = mutableListOf<suspend () -> Unit>()

    fun onCleanup(action: suspend () -> Unit) {
        callbacks += action
    }

    suspend fun cleanup() {
        callbacks.asReversed().forEach { it.invoke() }
    }
}
