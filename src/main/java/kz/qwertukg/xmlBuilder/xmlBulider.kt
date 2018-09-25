package kz.qwertukg.xmlBuilder

import java.lang.StringBuilder

@DslMarker
annotation class TagMarker

const val TAB = "    "

data class Attribute(val name: String, val value: String)

@TagMarker
interface Tag {
    var name: String
    val attributes: MutableList<Attribute>

    fun attr(name: String, value: String) = attributes.add(Attribute(name, value))

    fun render(margin: Int = 0): String

    fun renderOpeningTag() = StringBuilder().apply {
        append("<$name")
        attributes.forEach { append(" ${it.name}=\"${it.value}\"") }
        append(">")
    }
}

data class TagFather(override var name: String, override val attributes: MutableList<Attribute> = mutableListOf(), val tags: MutableList<Tag> = mutableListOf()) : Tag {
    fun tag(name: String, block: TagFather.() -> Unit) = tags.add(TagFather(name).apply(block))

    fun tag(name: String, value: String, block: TagValue.() -> Unit = {}) = tags.add(TagValue(name, value).apply(block))

    override fun render(margin: Int): String = StringBuilder().apply {
        appendln(TAB.repeat(margin) + renderOpeningTag())
        tags.forEach { appendln(it.render(margin + 1)) }
        append(TAB.repeat(margin) + "</$name>")
    }.toString()
}

data class TagValue(override var name: String, val value: String, override val attributes: MutableList<Attribute> = mutableListOf()) : Tag {
    override fun render(margin: Int) = StringBuilder().apply {
        append(TAB.repeat(margin) + renderOpeningTag())
        append(value)
        append("</$name>")
    }.toString()
}

fun tag(name: String, block: TagFather.() -> Unit) = TagFather(name).apply(block)