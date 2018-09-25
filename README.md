# xml-builder
Simplest XML builder for Kotlin

```kotlin
val xmlString = tag("html") {
    tag("head") {
        tag("title", "Is XML?")
    }
    tag("body") {
        tag("h1", "Hello world!")
        tag("p") {
            tag("a", "Kotlin") {
                attr("href", "https://kotlinlang.org")
                attr("target", "_blank")
            }
        }
        tag("p") {
            tag("img") { attr("src", "kotlin.png") }
        }
    }
}.render()
```
