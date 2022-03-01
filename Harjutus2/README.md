Kotlin on JVM-il põhinev keel ning Google'i poolt defakto Androidi arenduskeel, kuid on kasutuses ka teistes valdkondades, kus on näiteks asendatud Java või Scalaga (nt veebi backendid)

Võrreldes Javaga, on Kotlin:
- Verboossem
- Funktsionaalsem
- Vähendatud boilerplate'iga (eg ei ole vaja getterid/setterid ega Lomboki)
- Ühildub Javaga (Nt saab importida Java koodi Kotlini omasse)
- Kompileeritav JavaScriptisse

Sarnased keeled:
- Java
- Typescript ja Scala, kus tüübi definitsioonid on tagapool, nt ```fun MyFun(myVar: MyType): MyOtherType```
- Scala, oma funktsionaalsuse tõttu (ning Case klassid Scalas on põhimõtteliselt Data klassid Kotlinis)

## Võrdlused Javaga
### Muutujate deklareerimine
```java
// Java
int myNum = 5;
double myDouble = 5.99;
char myChar = 'D';
boolean myBool = true;
String myText = "Hello";

final double PI = 3.141592653589793; // konstantne
```

```kotlin
// Kotlin

// automaatsete tüüpidega
var myNum = 5
var myDouble = 5.99
var myChar = 'D'
var myBool = true
var myText = "Hello"
val PI = 3.141592653589793 // konstantne

// tüüpidega
var myNum = 5: Int
var myFloat = 5.99: Double
var myChar = 'D': Char
var myBool = true: Boolean
var myText = "Hello": String
val PI = 3.141592653589793: Double // konstantne
```

### Tingimuslaused
```java
// Java
if (20 > 18) {
  System.out.println("20 is greater than 18");
}

```

```kotlin
// Kotlin
if (20 > 18) {
  print("20 is greater than 18")
}

// Kotlinis saab tingimuse omistada ka muutujale

val number = -50

val result = if (number > 0) {
    "Positive number"
} else {
    "Negative number"
}
return result

// Lühendatud vormis sama koodijupp
val number = -50
val result = if (number > 0) "Positive number" else "Negative number"
return result

// Mõlemas nii Javas kui ka Kotlinis saab kasutada tenary operatorit
// result = (number > 0) ? "Positive number" : "Negative number"
```
### Tsüklid
```java
// Java
for (int i = 1; i < 3; i++) {
  System.println.out(i);
}

// Listid
for (int i = 0; i < arr.length; i++) {
    System.out.println("Element: " + arr[i]);
}

for (int i: arr) { // lühendatud
    System.out.println("Element: " + arr[i]);
}

// while
while (x > 0) {
    x--
}
```

```kotlin
// Kotlin

for (int i = 1; i < 3; i++) {
  println(i);
}

for (i in 1..3) {
    println(i)
}

// Listid
for (item in collection) print(item)

for (item: Int in ints) {
    // ...
}

for ((index, value) in array.withIndex()) {
    println("the element at $index is $value")
}

// while
while (x > 0) {
    x--
}
```

### Funktsioonid
```kotlin
//Kotlin

fun main() {
    // Who sent the most messages?
    val frequentSender = messages
        .groupBy(Message::sender)
        .maxByOrNull { (_, messages) -> messages.size }
        ?.key                                                 // Get their names
    println(frequentSender) // [Ma]

    // Who are the senders?
    val senders = messages
        .asSequence()                                         // Make operations lazy (for a long call chain)
        .filter { it.body.isNotBlank() && !it.isRead }        // Use lambdas...
        .map(Message::sender)                                 // ...or member references
        .distinct()
        .sorted()
        .toList()                                             // Convert sequence back to a list to get a result
    println(senders) // [Adam, Ma]
}

data class Message(                                          // Create a data class
    val sender: String,
    val body: String,
    val isRead: Boolean = false,                              // Provide a default value for the argument
)

val messages = listOf(                                       // Create a list
    Message("Ma", "Hey! Where are you?"),
    Message("Adam", "Everything going according to plan today?"),
    Message("Ma", "Please reply. I've lost you!"),
)
```
### Klassid

```kotlin
//Kotlin

abstract class Person(val name: String) {
    abstract fun greet()
}

interface FoodConsumer {
    fun eat()
    fun pay(amount: Int) = println("Delicious! Here's $amount bucks!")
}

class RestaurantCustomer(name: String, val dish: String) : Person(name), FoodConsumer {
    fun order() = println("$dish, please!")
    override fun eat() = println("*Eats $dish*")
    override fun greet() = println("It's me, $name.")
}

fun main() {
    val sam = RestaurantCustomer("Sam", "Mixed salad")
    sam.greet() // An implementation of an abstract function
    sam.order() // A member function
    sam.eat() // An implementation of an interface function
    sam.pay(10) // A default implementation in an interface
}
```

## Kotlin Standard Library võimalused

Kotlin stdlib annab paremaid võimalusi igapäevaste tegevusteks ning lisaks annab juurde teisi lisafunktsioone. Nad sisaldavad:

· Kõrgemat järku funktsioonid, mis rakendavad idiomaatilisi mustreid (lubamine, rakendamine, kasutamine, sünkroonimine jne ((let, apply, use, synchronized)).
· Laiendusfunktsioonid, mis pakuvad toiminguid päringute tegemiseks kogumitest (eager) ja jadadest (lazy).
· Erinevad utiliidid stringide ja char jadadega töötamiseks.
· JDK klasside laiendused, mis muudavad failide, IO ja lõimedega töötamise mugavaks. (threadid ja faililugemine)

Nad on jaotatud eraldi moodulitena

### package kotlin

- Sisaldab tüüpe, nt `Any`, `Array`, `Boolean`, `Char`, `Double` jne
- Abifunktsioonid, nt 
  - `emptyArray` 
  - `getOrThrow` 
  - `getOrElse`
  - `map`
  - `suspend`

Rohkem Kotlin core stdlib kohta [siin](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/)

### package kotlinx.browser

- Annab võimaluse kirjutada JS koodi Kotlinis
- Kood kompileeritav JavaScripti

```kotlin
val document: Document
```

```kotlin
val localStorage: Storage
```

```kotlin
val localStorage: Storage
```

```kotlin
val window: Window
```

### package kotlinx.dom

DOM-manipuleerimise funktsioonid ja komponendid

```kotlin
val Node.isElement: Boolean

```
```kotlin
val Node.isText: Boolean
```
```kotlin
fun Element.addClass(vararg cssClasses: String): Boolean
```
```kotlin
fun Element.appendElement(
    name: String,
    init: Element.() -> Unit
): Element
```
```kotlin
fun Element.appendText(text: String): Element
```
```kotlin
fun Node.clear()
```
```kotlin
fun Document.createElement(
    name: String,
    init: Element.() -> Unit
): Element
```
```kotlin
fun Element.hasClass(cssClass: String): Boolean
```
```kotlin
fun Element.removeClass(vararg cssClasses: String): Boolean
```

### ...ja teised
- kotlin.collections
- kotlin.annotation
- kotlin.comparisons
- kotlin.concurrent
- kotlin.coroutines
- kotlin.io
- kotlin.math
- ...

Terviklik nimekiri stdlib moodulitest [siin](https://kotlinlang.org/api/latest/jvm/stdlib/)
