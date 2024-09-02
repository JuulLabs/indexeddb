import com.juul.indexeddb.external.IDBKey
import com.juul.indexeddb.external.JsAny
import com.juul.indexeddb.jso
import com.juul.indexeddb.unsafeCast
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

private external interface InLineKeyUser : JsAny {
    var id: String
    var username: String
}

class InLineKeyObjectStore {

    @Test
    fun simpleReadWrite() = runTest {
        val database = openDatabase("in-line-keys", 1) { database, oldVersion, newVersion ->
            if (oldVersion < 1) {
                database.createObjectStore("users", KeyPath("id"))
            }
        }

        database.writeTransaction("users") {
            objectStore("users").add(
                jso<InLineKeyUser> {
                    id = "7740f7c4-f889-498a-bc6d-f88dabdcfb9a"
                    username = "Username"
                },
            )
        }

        val user = database.transaction("users") {
            objectStore("users")
                .get(IDBKey("7740f7c4-f889-498a-bc6d-f88dabdcfb9a"))
                ?.unsafeCast<InLineKeyUser>()
        }
        assertEquals("Username", user?.username)

        database.close()
        deleteDatabase("in-line-keys")
    }
}
