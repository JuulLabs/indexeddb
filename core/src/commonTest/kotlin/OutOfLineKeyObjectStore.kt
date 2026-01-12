package com.juul.indexeddb

import kotlin.js.toJsString
import kotlin.test.Test
import kotlin.test.assertEquals

class OutOfLineKeyObjectStore {

    @Test
    fun simpleReadWrite() = runTest {
        val database = openDatabase("out-of-line-keys", 1) { database, oldVersion, newVersion ->
            if (oldVersion < 1) {
                database.createObjectStore("users")
            }
        }
        onCleanup {
            database.close()
            deleteDatabase("out-of-line-keys")
        }

        database.writeTransaction("users") {
            objectStore(
                "users",
            ).add(jso<User> { username = "Username".toJsString() }, Key("7740f7c4-f889-498a-bc6d-f88dabdcfb9a".toJsString()))
        }

        val user = database.transaction("users") {
            objectStore("users")
                .get(Key("7740f7c4-f889-498a-bc6d-f88dabdcfb9a".toJsString()))
        } as User
        assertEquals("Username", user.username.toString())
    }
}
