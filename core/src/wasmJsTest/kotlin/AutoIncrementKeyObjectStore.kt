package com.juul.indexeddb

import kotlin.test.Test
import kotlin.test.assertEquals

private external interface User : JsAny {
    var username: JsString
}

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
            objectStore("users").add(jso<User> { username = "Username".toJsString() })
        }

        val user = database.transaction("users") {
            objectStore("users")
                .get(Key(id))
                ?.unsafeCast<User>()
        }
        assertEquals("Username", user?.username?.toString())
    }
}
