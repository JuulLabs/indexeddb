import com.juul.indexeddb.external.IDBKey
import com.juul.indexeddb.external.JsAny
import com.juul.indexeddb.jso
import com.juul.indexeddb.unsafeCast
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

private external interface OutOfLineKeyUser : JsAny {
    var username: String
}

class OutOfLineKeyObjectStore {

    @Test
    fun simpleReadWrite() = runTest {
        val database = openDatabase("out-of-line-keys", 1) { database, oldVersion, newVersion ->
            if (oldVersion < 1) {
                database.createObjectStore("users")
            }
        }

        database.writeTransaction("users") {
            objectStore("users")
                .add(
                    jso<OutOfLineKeyUser> { username = "Username" },
                    IDBKey("7740f7c4-f889-498a-bc6d-f88dabdcfb9a"),
                )
        }

        val user = database.transaction("users") {
            objectStore("users")
                .get(IDBKey("7740f7c4-f889-498a-bc6d-f88dabdcfb9a"))
                ?.unsafeCast<OutOfLineKeyUser>()
        }
        assertEquals("Username", user?.username)

        database.close()
        deleteDatabase("out-of-line-keys")
    }
}
