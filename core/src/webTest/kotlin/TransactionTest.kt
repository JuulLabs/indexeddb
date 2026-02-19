package com.juul.indexeddb

import kotlin.js.toJsString
import kotlin.js.toList
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class TransactionTest {
    @Test
    fun readWithinTransaction() = runTest {
        val database = openDatabase("read-within-transaction", 1) { database, oldVersion, newVersion ->
            if (oldVersion < 1) {
                database.createObjectStore("users", KeyPath("id"))
            }
        }
        onCleanup {
            database.close()
            deleteDatabase("read-within-transaction")
        }

        val user = database.writeTransaction("users") {
            objectStore("users").add(
                jso<User> {
                    id = "7740f7c4-f889-498a-bc6d-f88dabdcfb9a".toJsString()
                    username = "Username".toJsString()
                },
            )
            objectStore("users")
                .get(Key("7740f7c4-f889-498a-bc6d-f88dabdcfb9a".toJsString()))
        } as User

        assertEquals("Username", user.username.toString())
    }

    @Test
    fun whenExceptionIsThrowWithinTransaction_transactionIsAborted() = runTest {
        val database = openDatabase("abort-transaction", 1) { database, oldVersion, newVersion ->
            if (oldVersion < 1) {
                database.createObjectStore("users", KeyPath("id"))
            }
        }
        onCleanup {
            database.close()
            deleteDatabase("abort-transaction")
        }

        assertFailsWith<ExceptionToAbortTransaction> {
            database.writeTransaction("users") {
                objectStore("users").add(
                    jso<User> {
                        id = "7740f7c4-f889-498a-bc6d-f88dabdcfb9a".toJsString()
                        username = "Username".toJsString()
                    },
                )

                // Abort transaction
                throw ExceptionToAbortTransaction()
            }
        }

        // because transaction is aborted, new values shouldn't be stored
        val users = database.transaction("users") {
            objectStore("users").getAll()
        }

        assertEquals(emptyList(), users.toList())
    }
}
private class ExceptionToAbortTransaction : Exception()
