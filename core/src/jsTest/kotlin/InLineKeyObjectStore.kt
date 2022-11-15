package com.juul.indexeddb

import kotlin.test.Test
import kotlin.test.assertEquals

class InLineKeyObjectStore {

    @Test
    fun simpleReadWrite() = runTest {
        val database = openDatabase("in-line-keys", 1) { database, oldVersion, newVersion ->
            if (oldVersion < 1) {
                database.createObjectStore("users", KeyPath("id"))
            }
        }
        onCleanup {
            database.close()
            deleteDatabase("in-line-keys")
        }

        database.writeTransaction("users") {
            objectStore("users").add(jso { id = "7740f7c4-f889-498a-bc6d-f88dabdcfb9a"; username = "Username" })
        }

        val user = database.transaction("users") {
            objectStore("users")
                .get(Key("7740f7c4-f889-498a-bc6d-f88dabdcfb9a"))
        }
        assertEquals("Username", user.username)
    }
}
