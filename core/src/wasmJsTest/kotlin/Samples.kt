package com.juul.indexeddb

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import kotlin.test.Test
import kotlin.test.assertEquals

// This file is not a good unit test. Instead, it serves as proof of the README's usage sample.

external interface Customer : JsAny {
    var ssn: JsString
    var name: JsString
    var age: JsNumber
    var email: JsString
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
        onCleanup {
            database.close()
            deleteDatabase("your-database-name")
        }

        database.writeTransaction("customers") {
            val store = objectStore("customers")
            store.add(
                jso<Customer> {
                    ssn = "333-33-3333".toJsString()
                    name = "Alice".toJsString()
                    age = 33.toJsNumber()
                    email = "alice@company.com".toJsString()
                },
            )
            store.add(
                jso<Customer> {
                    ssn = "444-44-4444".toJsString()
                    name = "Bill".toJsString()
                    age = 35.toJsNumber()
                    email = "bill@company.com".toJsString()
                },
            )
            store.add(
                jso<Customer> {
                    ssn = "555-55-5555".toJsString()
                    name = "Charlie".toJsString()
                    age = 29.toJsNumber()
                    email = "charlie@home.org".toJsString()
                },
            )
            store.add(
                jso<Customer> {
                    ssn = "666-66-6666".toJsString()
                    name = "Donna".toJsString()
                    age = 31.toJsNumber()
                    email = "donna@home.org".toJsString()
                },
            )
        }

        val bill = database.transaction("customers") {
            objectStore("customers").get(Key("444-44-4444".toJsString())) as Customer
        }
        assertEquals("Bill", bill.name.toString())

        val donna = database.transaction("customers") {
            objectStore("customers").index("age").get(bound(30.toJsNumber(), 32.toJsNumber())) as Customer
        }
        assertEquals("Donna", donna.name.toString())

        val charlie = database.transaction("customers") {
            objectStore("customers")
                .index("name")
                .openCursor(autoContinue = true)
                .map { it.value as Customer }
                .first { it.age.toInt() < 32 }
        }
        assertEquals("Charlie", charlie.name.toString())

        val count = database.transaction("customers") {
            objectStore("customers").count()
        }
        assertEquals(4, count.toInt())

        val countBelowThirtyTwo = database.transaction("customers") {
            objectStore("customers").index("age").count(upperBound(32.toJsNumber()))
        }
        assertEquals(2, countBelowThirtyTwo.toInt())

        val skipTwoYoungest = database.transaction("customers") {
            objectStore("customers")
                .index("age")
                .openCursor(cursorStart = CursorStart.Advance(2), autoContinue = true)
                .map { it.value as Customer }
                .map { it.name }
                .toList()
        }
        assertEquals(listOf("Alice", "Bill"), skipTwoYoungest.map { it.toString() })

        val skipUntil33 = database.transaction("customers") {
            objectStore("customers")
                .index("age")
                .openCursor(cursorStart = CursorStart.Continue(Key(33.toJsNumber())), autoContinue = true)
                .map { it.value as Customer }
                .map { it.name }
                .toList()
        }
        assertEquals(listOf("Alice", "Bill"), skipUntil33.map { it.toString() })
    }
}
