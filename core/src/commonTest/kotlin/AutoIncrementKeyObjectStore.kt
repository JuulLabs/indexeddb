import com.juul.indexeddb.external.IDBKey
import com.juul.indexeddb.external.JsAny
import com.juul.indexeddb.jso
import com.juul.indexeddb.unsafeCast
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

private external interface User : JsAny {
    var username: String
}

class AutoIncrementKeyObjectStore {
    @Test
    fun simpleReadWrite() = runTest {
        val database = openDatabase("auto-increment-keys", 1) { database, oldVersion, newVersion ->
            if (oldVersion < 1) {
                database.createObjectStore("users", AutoIncrement)
            }
        }

        val id = database.writeTransaction("users") {
            objectStore("users").add(jso<User> { username = "Username" })
        }

        val user = database.transaction("users") {
            objectStore("users")
                .get(IDBKey(id))
                ?.unsafeCast<User>()
        }
        assertEquals("Username", user?.username)

        database.close()
        deleteDatabase("auto-increment-keys")
    }
}
