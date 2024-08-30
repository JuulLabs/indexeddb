package com.juul.indexeddb

import kotlin.test.Test
import kotlin.test.assertEquals

private external interface OutOfLineKeyUser : JsAny {
    var username: JsString
}

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
            objectStore("users")
                .add(
                    jso<OutOfLineKeyUser> { username = "Username".toJsString() },
                    Key("7740f7c4-f889-498a-bc6d-f88dabdcfb9a".toJsString()),
                )
        }

        val user = database.transaction("users") {
            objectStore("users")
                .get(Key("7740f7c4-f889-498a-bc6d-f88dabdcfb9a".toJsString()))
                ?.unsafeCast<OutOfLineKeyUser>()
        }
        assertEquals("Username", user?.username?.toString())
    }
}
