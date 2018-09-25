package kz.qwertukg.xmlBuilder

import org.junit.Assert.*
import org.junit.Test

class BasicTest {

    @Test
    fun `Check is XML`() {
        val actualXml = tag("html") {
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
                    tag("img") {
                        attr("src", "kotlin.png")
                    }
                }
            }
        }.render()

        val expectedXml = """
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
                        <img src="kotlin.png">
                        </img>
                    </p>
                </body>
            </html>
        """.trimIndent().replace("\n", System.lineSeparator())

        assertEquals(expectedXml, actualXml)
    }

}