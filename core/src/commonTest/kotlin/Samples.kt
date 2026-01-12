package com.juul.indexeddb

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import kotlin.js.JsAny
import kotlin.js.JsNumber
import kotlin.js.JsString
import kotlin.js.toInt
import kotlin.js.toJsNumber
import kotlin.js.toJsString
import kotlin.test.Test
import kotlin.test.assertEquals

// This file is not a good unit test. Instead, it serves as proof of the README's usage sample.

external interface JsCustomer : JsAny {
    var ssn: JsString
    var name: JsString
    var age: JsNumber
    var email: JsString
}

data class Customer(
    val ssn: String,
    val name: String,
    val age: Int,
    val email: String,
) {

    constructor(js: JsCustomer) : this(
        ssn = js.ssn.toString(),
        name = js.name.toString(),
        age = js.age.toInt(),
        email = js.email.toString(),
    )

    fun toJs(): JsCustomer {
        val js = jso<JsCustomer>()
        js.ssn = ssn.toJsString()
        js.name = name.toJsString()
        js.age = age.toJsNumber()
        js.email = email.toJsString()
        return js
    }
}

fun JsCustomer.toKotlin(): Customer = Customer(this)

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
            }
        }
        onCleanup {
            database.close()
            deleteDatabase("your-database-name")
        }

        database.writeTransaction("customers") {
            val store = objectStore("customers")
            store.add(Customer(ssn = "333-33-3333", name = "Alice", age = 33, email = "alice@company.com").toJs())
            store.add(Customer(ssn = "444-44-4444", name = "Bill", age = 35, email = "bill@company.com").toJs())
            store.add(Customer(ssn = "555-55-5555", name = "Charlie", age = 29, email = "charlie@home.org").toJs())
            store.add(Customer(ssn = "666-66-6666", name = "Donna", age = 31, email = "donna@home.org").toJs())
        }

        val bill = database
            .transaction("customers") {
                objectStore("customers").get(Key("444-44-4444".toJsString())) as JsCustomer
            }.toKotlin()
        assertEquals("Bill", bill.name)

        val donna = database
            .transaction("customers") {
                objectStore("customers").index("age").get(bound(30.toJsNumber(), 32.toJsNumber())) as JsCustomer
            }.toKotlin()
        assertEquals("Donna", donna.name)

        val charlie = database.transaction("customers") {
            objectStore("customers")
                .index("name")
                .openCursor()
                .map { (it.value as JsCustomer).toKotlin() }
                .first { it.age < 32 }
        }
        assertEquals("Charlie", charlie.name)

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
                .map { it.value as JsCustomer }
                .map { it.toKotlin().name }
                .toList()
        }
        assertEquals(listOf("Alice", "Bill"), skipTwoYoungest)

        val skipUntil33 = database.transaction("customers") {
            objectStore("customers")
                .index("age")
                .openCursor(cursorStart = CursorStart.Continue(Key(33.toJsNumber())), autoContinue = true)
                .map { it.value as JsCustomer }
                .map { it.toKotlin().name }
                .toList()
        }
        assertEquals(listOf("Alice", "Bill"), skipUntil33)
    }
}
