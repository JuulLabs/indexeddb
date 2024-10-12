import com.juul.indexeddb.external.IDBKey
import com.juul.indexeddb.external.JsAny
import com.juul.indexeddb.external.toJsNumber
import com.juul.indexeddb.jso
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

// This file is not a good unit test. Instead, it serves as proof of the README's usage sample.

external interface Customer : JsAny {
    var ssn: String
    var name: String
    var age: Int
    var email: String
}

@Suppress("UNCHECKED_CAST_TO_EXTERNAL_INTERFACE")
class Samples {

    @Test
    fun simpleReadWrite() = runTest {
        val database = openDatabase("your-database-name", 1) { database, oldVersion, newVersion ->
            if (oldVersion < 1) {
                val store = database.createObjectStore("customers", KeyPath("ssn"))
                store.createIndex("name", KeyPath("name"), unique = false)
                store.createIndex("age", KeyPath("age"), unique = false)
                store.createIndex("email", KeyPath("email"), unique = true)
                store.createIndex("unnecessary_index", KeyPath("unimporatant"), unique = true)
                store.deleteIndex("unnecessary_index")
            }
        }

        database.writeTransaction("customers") {
            val store = objectStore("customers")
            store.add(
                jso<Customer> {
                    ssn = "333-33-3333"
                    name = "Alice"
                    age = 33
                    email = "alice@company.com"
                },
            )
            store.add(
                jso<Customer> {
                    ssn = "444-44-4444"
                    name = "Bill"
                    age = 35
                    email = "bill@company.com"
                },
            )
            store.add(
                jso<Customer> {
                    ssn = "555-55-5555"
                    name = "Charlie"
                    age = 29
                    email = "charlie@home.org"
                },
            )
            store.add(
                jso<Customer> {
                    ssn = "666-66-6666"
                    name = "Donna"
                    age = 31
                    email = "donna@home.org"
                },
            )
        }

        val bill = database.transaction("customers") {
            objectStore("customers").get(IDBKey("444-44-4444")) as Customer
        }
        assertEquals("Bill", bill.name)

        val donna = database.transaction("customers") {
            objectStore("customers").index("age").get(bound(30.toJsNumber(), 32.toJsNumber())) as Customer
        }
        assertEquals("Donna", donna.name)

        val charlie = database.transaction("customers") {
            objectStore("customers")
                .index("name")
                .openCursor(autoContinue = true)
                .map { it.value as Customer }
                .first { it.age < 32 }
        }
        assertEquals("Charlie", charlie.name)

        val count = database.transaction("customers") {
            objectStore("customers").count()
        }
        assertEquals(4, count)

        val countBelowThirtyTwo = database.transaction("customers") {
            objectStore("customers").index("age").count(upperBound(32.toJsNumber()))
        }
        assertEquals(2, countBelowThirtyTwo)

        val skipTwoYoungest = database.transaction("customers") {
            objectStore("customers")
                .index("age")
                .openCursor(cursorStart = CursorStart.Advance(2), autoContinue = true)
                .map { it.value as Customer }
                .map { it.name }
                .toList()
        }
        assertEquals(listOf("Alice", "Bill"), skipTwoYoungest.map { it })

        val skipUntil33 = database.transaction("customers") {
            objectStore("customers")
                .index("age")
                .openCursor(cursorStart = CursorStart.Continue(IDBKey(33)), autoContinue = true)
                .map { it.value as Customer }
                .map { it.name }
                .toList()
        }
        assertEquals(listOf("Alice", "Bill"), skipUntil33.map { it })

        database.close()
        deleteDatabase("your-database-name")
    }
}
