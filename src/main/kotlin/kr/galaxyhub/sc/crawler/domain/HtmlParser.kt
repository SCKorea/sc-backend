package kr.galaxyhub.sc.crawler.domain

fun interface HtmlParser {

    fun parse(contents: List<String>): String
}
