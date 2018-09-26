package kz.qwertukg.xmlBuilder

import java.lang.StringBuilder

@DslMarker
annotation class TagMarker

const val TAB = "    "
val RN = System.lineSeparator()!!

data class Attribute(val name: String, val value: String)

@TagMarker
interface Tag {
    var name: String
    val attributes: MutableList<Attribute>

    fun attr(name: String, value: String) = attributes.add(Attribute(name.clean(), value.escape()))

    fun render(margin: Int = 0): String

    fun renderStartTeg(isClosed: Boolean = false) = StringBuilder().apply {
        append("<$name")
        attributes.forEach { append(" ${it.name}=\"${it.value}\"") }
        if (isClosed) append("/>") else append(">")
    }
}

data class TagFather(override var name: String, override val attributes: MutableList<Attribute> = mutableListOf(), val tags: MutableList<Tag> = mutableListOf()) : Tag {
    var declaration = ""

    fun tag(name: String, block: TagFather.() -> Unit = {}) = tags.add(TagFather(name.clean()).apply(block))

    fun tag(name: String, value: String, block: TagValue.() -> Unit = {}) = tags.add(TagValue(name.clean(), value.escape()).apply(block))

    override fun render(margin: Int): String = StringBuilder().apply {
        if (declaration.isNotBlank()) {
            appendln(declaration)
            declaration = ""
        }
        appendln(TAB.repeat(margin) + renderStartTeg(tags.isEmpty()))
        if (tags.isNotEmpty()) {
            tags.forEach { appendln(it.render(margin + 1)) }
            append(TAB.repeat(margin) + "</$name>")
        }
    }.toString().replace(RN + RN, RN) // WTF?
}

data class TagValue(override var name: String, val value: String, override val attributes: MutableList<Attribute> = mutableListOf()) : Tag {
    override fun render(margin: Int) = StringBuilder().apply {
        append(TAB.repeat(margin) + renderStartTeg(value.isBlank()))
        if (value.isNotBlank()) {
            append(value)
            append("</$name>")
        }
    }.toString()
}

fun tag(name: String, version: String = "1.0", encoding: String = "UTF-8", block: TagFather.() -> Unit) = TagFather(name.clean()).apply {
    declaration = """<?xml version="${version.escape()}" encoding="${encoding.escape()}"?>"""
    block()
}

private fun String.clean() = replace(Regex("[\\\\!\"#\$%&'()*+,/;<=>?@\\[\\]^`{|}~ ]"), "")
        .replace(Regex("^([-.])+"), "")

private fun String.escape() = replace("<", "&lt;")
        .replace(">", "&gt;")
        .replace("&", "&amp;")
        .replace("'", "&apos;")
        .replace("\"", "&quot;")
