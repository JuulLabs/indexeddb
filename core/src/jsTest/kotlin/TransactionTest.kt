package com.juul.indexeddb

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
                jso {
                    id = "7740f7c4-f889-498a-bc6d-f88dabdcfb9a"
                    username = "Username"
                },
            )
            objectStore("users")
                .get(Key("7740f7c4-f889-498a-bc6d-f88dabdcfb9a"))
        }

        assertEquals("Username", user.username)
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
                    jso {
                        id = "7740f7c4-f889-498a-bc6d-f88dabdcfb9a"
                        username = "Username"
                    },
                )

                // Abort transaction
                throw ExceptionToAbortTransaction()
            }
        }

        // because transaction is aborted, new values shouldn't be stored
        val users = database.transaction("users") {
            objectStore("users").getAll().toList()
        }

        assertEquals(listOf(), users)
    }
}
private class ExceptionToAbortTransaction : Exception()
