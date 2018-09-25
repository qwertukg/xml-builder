# Kotlin XML builder
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
Will be: 
```xml
<?xml version="1.0" encoding="UTF-8"?>
<html>
    <head>
        <title>Is XML?</title>
    </head>
    <body>
        <h1>Hello world!</h1>
        <p>
            <a href="https://kotlinlang.org" target="_blank">Kotlin</a>
        </p>
        <p>
            <img src="kotlin.png"/>
        </p>
    </body>
</html>
```
