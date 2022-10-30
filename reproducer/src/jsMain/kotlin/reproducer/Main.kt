package reproducer

import com.juul.indexeddb.Key
import com.juul.indexeddb.KeyPath
import com.juul.indexeddb.openDatabase
import kotlinx.browser.window
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlin.js.Date

fun main() {
    window.onload = {
        MainScope().launch {
            val database = openDatabase("test", 1) { database, old, new ->
                if (old < 1) {
                    database.createObjectStore("store", KeyPath("id"))
                }
            }
            database.writeTransaction("store") {
                val store = objectStore("store")
                store.clear()
                store.add(jso<Data> { id = 0; name = "a" })
                store.add(jso<Data> { id = 1; name = "b" })
                store.add(jso<Data> { id = 2; name = "c" })
                store.add(jso<Data> { id = 3; name = "d" })
                store.add(jso<Data> { id = 4; name = "e" })
                store.add(jso<Data> { id = 5; name = "f" })
                store.add(jso<Data> { id = 6; name = "g" })
                store.add(jso<Data> { id = 7; name = "h" })
                store.add(jso<Data> { id = 8; name = "i" })
                store.add(jso<Data> { id = 9; name = "j" })
            }
            database.writeTransaction("store") {
                val store = objectStore("store")
                store.openCursor()
                    .collect { cursor ->
                        console.log("1: cursor.value = ${JSON.stringify(cursor.value, null, "  ")}")
                        console.log("2: delete()")
                        cursor.delete()
                        console.log("3: cursor.value = ${JSON.stringify(cursor.value, null, "  ")}")
                        console.log("4: store.get()")
                        store.get(Key("other"))
                        console.log("5: cursor.value = ${JSON.stringify(cursor.value, null, "  ")}")
                        console.log("6: store.get()")
                        store.get(Key("other"))
                        console.log("7: cursor.value = ${JSON.stringify(cursor.value, null, "  ")}")
                    }
            }
        }
    }
}

fun <T : Any> jso(): T = js("({})") as T
inline fun <T : Any> jso(block: T.() -> Unit): T = jso<T>().apply(block)

external interface Data {
    var id: Int
    var name: String
}
