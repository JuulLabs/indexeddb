package com.juul.indexeddb

import kotlin.test.Test
import kotlin.test.assertEquals

class AutoIncrementKeyObjectStore {

    @Test
    fun simpleReadWrite() = runTest {
        val database = openDatabase("auto-increment-keys", 1) { database, oldVersion, newVersion ->
            if (oldVersion < 1) {
                database.createObjectStore("users", AutoIncrement)
            }
        }
        onCleanup {
            database.close()
            deleteDatabase("auto-increment-keys")
        }

        val id = database.writeTransaction("users") {
            objectStore("users").add(jso { username = "Username" }) as Double
        }

        val user = database.transaction("users") {
            objectStore("users")
                .get(Key(id))
        }
        assertEquals("Username", user.username)
    }
}
