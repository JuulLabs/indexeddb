![badge][badge-js]
[![Slack](https://img.shields.io/badge/Slack-%23juul--libraries-ECB22E.svg?logo=data:image/svg+xml;base64,PHN2ZyB2aWV3Qm94PSIwIDAgNTQgNTQiIHhtbG5zPSJodHRwOi8vd3d3LnczLm9yZy8yMDAwL3N2ZyI+PGcgZmlsbD0ibm9uZSIgZmlsbC1ydWxlPSJldmVub2RkIj48cGF0aCBkPSJNMTkuNzEyLjEzM2E1LjM4MSA1LjM4MSAwIDAgMC01LjM3NiA1LjM4NyA1LjM4MSA1LjM4MSAwIDAgMCA1LjM3NiA1LjM4Nmg1LjM3NlY1LjUyQTUuMzgxIDUuMzgxIDAgMCAwIDE5LjcxMi4xMzNtMCAxNC4zNjVIMS4zNzZBNS4zODEgNS4zODEgMCAwIDAgMCAxOS44ODRhNS4zODEgNS4zODEgMCAwIDAgNS4zNzYgNS4zODdoMTQuMzM2YTUuMzgxIDUuMzgxIDAgMCAwIDUuMzc2LTUuMzg3IDUuMzgxIDUuMzgxIDAgMCAwLTUuMzc2LTUuMzg2IiBmaWxsPSIjMzZDNUYwIi8+PHBhdGggZD0iTTUzLjc2IDE5Ljg4NGE1LjM4MSA1LjM4MSAwIDAgMC01LjM3Ni01LjM4NiA1LjM4MSA1LjM4MSAwIDAgMC01LjM3NiA1LjM4NnY1LjM4N2g1LjM3NmE1LjM4MSA1LjM4MSAwIDAgMCA1LjM3Ni01LjM4N20tMTQuMzM2IDBWNS41MkE1LjM4MSA1LjM4MSAwIDAgMCAzNC4wNDguMTMzYTUuMzgxIDUuMzgxIDAgMCAwLTUuMzc2IDUuMzg3djE0LjM2NGE1LjM4MSA1LjM4MSAwIDAgMCA1LjM3NiA1LjM4NyA1LjM4MSA1LjM4MSAwIDAgMCA1LjM3Ni01LjM4NyIgZmlsbD0iIzJFQjY3RCIvPjxwYXRoIGQ9Ik0zNC4wNDggNTRhNS4zODEgNS4zODEgMCAwIDAgNS4zNzYtNS4zODcgNS4zODEgNS4zODEgMCAwIDAtNS4zNzYtNS4zODZoLTUuMzc2djUuMzg2QTUuMzgxIDUuMzgxIDAgMCAwIDM0LjA0OCA1NG0wLTE0LjM2NWgxNC4zMzZhNS4zODEgNS4zODEgMCAwIDAgNS4zNzYtNS4zODYgNS4zODEgNS4zODEgMCAwIDAtNS4zNzYtNS4zODdIMzQuMDQ4YTUuMzgxIDUuMzgxIDAgMCAwLTUuMzc2IDUuMzg3IDUuMzgxIDUuMzgxIDAgMCAwIDUuMzc2IDUuMzg2IiBmaWxsPSIjRUNCMjJFIi8+PHBhdGggZD0iTTAgMzQuMjQ5YTUuMzgxIDUuMzgxIDAgMCAwIDUuMzc2IDUuMzg2IDUuMzgxIDUuMzgxIDAgMCAwIDUuMzc2LTUuMzg2di01LjM4N0g1LjM3NkE1LjM4MSA1LjM4MSAwIDAgMCAwIDM0LjI1bTE0LjMzNi0uMDAxdjE0LjM2NEE1LjM4MSA1LjM4MSAwIDAgMCAxOS43MTIgNTRhNS4zODEgNS4zODEgMCAwIDAgNS4zNzYtNS4zODdWMzQuMjVhNS4zODEgNS4zODEgMCAwIDAtNS4zNzYtNS4zODcgNS4zODEgNS4zODEgMCAwIDAtNS4zNzYgNS4zODciIGZpbGw9IiNFMDFFNUEiLz48L2c+PC9zdmc+&labelColor=611f69)](https://kotlinlang.slack.com/messages/juul-libraries/)

# Kotlin IndexedDB

A wrapper around [IndexedDB] which allows for access from Kotlin/JS and Kotlin/WASM code using `suspend` blocks and linear, non-callback
based control flow.

## Migration Notes

In IndexedDB 0.12.0, support for Kotlin/WASM was added. This change removed all usages of `dynamic` in favor of `JsAny`.
Often `JsAny` will be expressed via a `typealias`, such as `IDBKey`, where the alias name and documentation can provide
additional context for expected values of that type.

On Kotlin/JS, `JsAny` is a typealias for `Any` so no changes are required for _inputs_ to IndexedDB. However, _outputs_
from IndexedDB might require casting via `asDynamic()` if you access fields on them directly. If you already defined
`external interface`s as interop types and cast outputs to those types, you are likely insulated from most changes.

On Kotlin/WASM (or common code targeting both JS and WASM), `JsAny` is a genuine type bound that must be satisfied. This
means that `external interface`s must extend from `JsAny` to be used as inputs or cast from outputs.

## Usage

The samples for usage here loosely follows several examples in [Using IndexedDB]. As such, we'll define our example data
type to match.

**Important:** our database type is defined as an `external interface`, which guarantees that the Kotlin compiler emits
a plain JavaScript object for it.

```kotlin
external interface JsCustomer : JsAny {
    var ssn: String
    var name: String
    var age: Int
    var email: String
}
```

Because external interfaces are missing several niceties you might expect from a `data class`, it often makes sense
to define a data class with interop functions to convert between Kotlin-first and JS-first representations.

<details>
<summary>Click to expand</summary>

```kotlin
private fun <T: JsAny> emptyObject(): T = js("{}") as T

data class Customer(
    val ssn: String,
    val name: String,
    val age: Int,
    val email: String,
) {

    constructor(js: JsCustomer) : this(
        ssn = js.ssn,
        name = js.name,
        age = js.age,
        email = js.email,
    )

    fun toJs(): JsCustomer {
        val js = emptyObject<JsCustomer>()
        js.ssn = ssn
        js.name = name
        js.age = age
        js.email = email
        return js
    }
}

fun JsCustomer.toKotlin(): Customer = Customer(this)
```

</details>

Note that `JsAny` and friends are currently considered experimental. To opt-in to these project wide, add the following
to your `gradle.properties`:

```kotlin
kotlin {
    sourceSets {
        all {
            compilerOptions.optIn.add("kotlin.js.ExperimentalWasmJsInterop")
        }
    }
}
```

### Available Types

Care must be taken to ensure that all fields passed into IndexedDB are of types understood by the database.

For [keys](https://developer.mozilla.org/en-US/docs/Web/API/IndexedDB_API/Basic_Terminology#key), JavaScript's `string`,
`date`, `number`, `Blob`, and arrays of these types may be used. Some additional types can be used inside of
[values](https://developer.mozilla.org/en-US/docs/Web/API/IndexedDB_API/Basic_Terminology#value): `boolean`, `object`,
`regexp`, `undefined`, and `null`.

Inside of an `external interface`, Kotlin will reject most types at compile time. However, some types are allowed
by the compiler that are extremely likely to crash at runtime (such as `Long`, which compiles into the unsupported
`bigint`). Note that usage of incorrect types is not currently detected by this library. Type related failures are
likely to manifest as either class cast exceptions or internal errors directly from JavaScript.

### Creation & Migration

Creating a [`Database`] and handling migrations are done together with the [`openDatabase`] function. The database name and
desired version are passed in as arguments. If the desired version and the current version match, then the callback is
not called. Otherwise, the callback is called in a [`VersionChangeTransaction`] scope. Generally, a chain of `if` blocks
checking the `oldVersion` are sufficient for handling migrations, including migration from version `0` to `1`:

```kotlin
val database = openDatabase("your-database-name", 1) { database, oldVersion, newVersion ->
    if (oldVersion < 1) {
        val store = database.createObjectStore("customers", KeyPath("ssn"))
        store.createIndex("name", KeyPath("name"), unique = false)
        store.createIndex("age", KeyPath("age"), unique = false)
        store.createIndex("email", KeyPath("email"), unique = true)
    }
}
```

Transactions, such as the lambda block of `openData`, are handled as `suspend` functions but with an important constraint:
**you must not call any `suspend` functions except for those provided by this library and scoped on [`Transaction`] (and
its subclasses), and flow operations on the flow returned by [`Transaction.openCursor`]**. Of course, it is also okay to
call `suspend` functions which only suspend by calling other legal functions.

This constraint is forced by the design of IndexedDB auto-committing transactions when it detects no remaining callbacks,
and failure to adhere to this can cause `TransactionInactiveError` to be thrown.

### Writing Data

To add data to the [`Database`] created above, open a [`WriteTransaction`], and then open the [`ObjectStore`]. Use
[`WriteTransaction.add`] to guarantee insert-only behavior, and use  [`WriteTransaction.put`] for insert-or-update.

Note that transactions must explicitly request every [`ObjectStore`] they reference at time of opening the transaction,
even if the store is only used conditionally. Multiple [`WriteTransaction`] which share referenced [`ObjectStore`] will
not be executed concurrently.

```kotlin
database.writeTransaction("customers") {
    val store = objectStore("customers")
    store.add(Customer(ssn = "333-33-3333", name = "Alice", age = 33, email = "alice@company.com").toJs())
    store.add(Customer(ssn = "444-44-4444", name = "Bill", age = 35, email = "bill@company.com").toJs())
    store.add(Customer(ssn = "555-55-5555", name = "Charlie", age = 29, email = "charlie@home.org").toJs())
    store.add(Customer(ssn = "666-66-6666", name = "Donna", age = 31, email = "donna@home.org").toJs())
}
```

### Reading Data

To read data, open a [`Transaction`], and then open the [`ObjectStore`]. Use [`Transaction.get`] and [`Transaction.getAll`]
to retrieve single items and retrieve bulk items, respectively.

As above, all object stores potentially used must be specified in advance. Unlike [`WriteTransaction`], multiple read-only
[`Transaction`] which share an [`ObjectStore`] can operate concurrently, but they still cannot operate concurrently with
a [`WriteTransaction`] sharing that store.

```kotlin
val bill = database.transaction("customers") {
    objectStore("customers").get(Key("444-44-4444")) as JsCustomer
}.toKotlin()
assertEquals("Bill", bill.name)
```

#### Key Ranges and Indices

With an [`ObjectStore`] you can query on a previously created [`Index`] instead of the primary key. This is especially
useful in combination with key ranges, and together more powerful queries can be constructed.

Three standard key ranges exist: [`lowerBound`], [`upperBound`], and [`bound`] (which combines the two). **Warning:**
key range behavior on an array-typed index can have potentially unexpected behavior. As an example, the key `[3, 0]` is
included in `bound(arrayOf(2, 2), arrayOf(4, 4))`.

```kotlin
val donna = database.transaction("customers") {
    objectStore("customers").index("age").get(bound(30.toJsNumber(), 32.toJsNumber())) as JsCustomer
}.toKotlin()
assertEquals("Donna", donna.name)
```

### Cursors

Cursors are excellent for optimizing complex queries. With either [`ObjectStore`] or [`Index`], call
[`Transaction.openCursor`] to return a `Flow` of [`CursorWithValue`] which emits once per row matching the query. The
returned flow is cold and properly handles early collection termination. To get the value of the row currently pointed
at by the cursor, call [`CursorWithValue.value`].

As an example we can find the first customer alphabetically with an age under 32:

```kotlin
val charlie = database.transaction("customers") {
    objectStore("customers")
        .index("name")
        .openCursor(autoContinue = true)
        .map { it.value as JsCustomer }
        .first { it.age < 32.toJsNumber() }
}.toKotlin()
assertEquals("Charlie", charlie.name)
```

Cursors can also be used to update or delete the value at the current index by calling [`WriteTransaction.update`] and
[`WriteTransaction.delete`], respectively.

## Setup

### Gradle

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.juul.indexeddb/core/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.juul.indexeddb/core)

IndexedDB can be configured via Gradle Kotlin DSL as follows:

```kotlin
repositories {
    mavenCentral()
}

dependencies {
    implementation("com.juul.indexeddb:core:$version")
}
```

If you prefer to work with the raw JavaScript API instead of the `suspend`-type wrappers, replace the implementation with `com.juul.indexeddb:external:$version`.

# License

```
Copyright 2026 JUUL Labs, Inc.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```

[//]: # (Internal class and member references)
[`CursorWithValue`]: https://juullabs.github.io/indexeddb/core/core/com.juul.indexeddb/-cursor-with-value/index.html
[`CursorWithValue.value`]: https://juullabs.github.io/indexeddb/core/core/com.juul.indexeddb/-cursor-with-value/value.html
[`Database`]: https://juullabs.github.io/indexeddb/core/core/com.juul.indexeddb/-database/index.html
[`Index`]: https://juullabs.github.io/indexeddb/core/core/com.juul.indexeddb/-index/index.html
[`ObjectStore`]: https://juullabs.github.io/indexeddb/core/core/com.juul.indexeddb/-object-store/index.html
[`Transaction`]: https://juullabs.github.io/indexeddb/core/core/com.juul.indexeddb/-transaction/index.html
[`Transaction.get`]: https://juullabs.github.io/indexeddb/core/core/com.juul.indexeddb/-transaction/get.html
[`Transaction.getAll`]: https://juullabs.github.io/indexeddb/core/core/com.juul.indexeddb/-transaction/get-all.html
[`Transaction.openCursor`]: https://juullabs.github.io/indexeddb/core/core/com.juul.indexeddb/-transaction/open-cursor.html
[`VersionChangeTransaction`]: https://juullabs.github.io/indexeddb/core/core/com.juul.indexeddb/-version-change-transaction/index.html
[`WriteTransaction`]: https://juullabs.github.io/indexeddb/core/core/com.juul.indexeddb/-write-transaction/index.html
[`WriteTransaction.add`]: https://juullabs.github.io/indexeddb/core/core/com.juul.indexeddb/-write-transaction/add.html
[`WriteTransaction.delete`]: https://juullabs.github.io/indexeddb/core/core/com.juul.indexeddb/-write-transaction/delete.html
[`WriteTransaction.put`]: https://juullabs.github.io/indexeddb/core/core/com.juul.indexeddb/-write-transaction/put.html
[`WriteTransaction.update`]: https://juullabs.github.io/indexeddb/core/core/com.juul.indexeddb/-write-transaction/update.html
[//]: # (Internal top-level function references)
[`bound`]: https://juullabs.github.io/indexeddb/core/core/com.juul.indexeddb/bound.html
[`lowerBound`]: https://juullabs.github.io/indexeddb/core/core/com.juul.indexeddb/lower-bound.html
[`openDatabase`]: https://juullabs.github.io/indexeddb/core/core/com.juul.indexeddb/open-database.html
[`upperBound`]: https://juullabs.github.io/indexeddb/core/core/com.juul.indexeddb/upper-bound.html
[//]: # (External references)
[IndexedDB]: https://developer.mozilla.org/en-US/docs/Web/API/IndexedDB_API
[Using IndexedDB]: https://developer.mozilla.org/en-US/docs/Web/API/IndexedDB_API/Using_IndexedDB
[//]: # (Images)
[badge-js]: http://img.shields.io/badge/platform-js-F8DB5D.svg?style=flat
