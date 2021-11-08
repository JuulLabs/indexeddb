package com.juul.indexeddb

import kotlinext.js.jsObject
import kotlin.test.Test
import kotlin.test.assertEquals

// This file is not a good unit test. Instead, it serves as proof of the README's usage sample.

external interface Customer {
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
            }
        }

        database.writeTransaction("customers") {
            val store = objectStore("customers")
            store.add(jsObject<Customer> { ssn = "444-44-4444"; name = "Bill"; age = 35; email = "bill@company.com" })
            store.add(jsObject<Customer> { ssn = "555-55-5555"; name = "Donna"; age = 32; email = "donna@home.org" })
        }

        val bill = database.transaction("customers") {
            objectStore("customers").get(Key("444-44-4444")) as Customer
        }
        assertEquals("Bill", bill.name)

        val donna = database.transaction("customers") {
            objectStore("customers").index("age").get(upperBound(34)) as Customer
        }
        assertEquals("Donna", donna.name)
    }
}
