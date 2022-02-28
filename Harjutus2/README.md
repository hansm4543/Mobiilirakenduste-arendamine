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

```

### Funktsioonid

### Klassid

### Kotlin Standard Library võimalused