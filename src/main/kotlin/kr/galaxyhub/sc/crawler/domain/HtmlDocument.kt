package kr.galaxyhub.sc.crawler.domain

data class HtmlDocument(
    val head: List<HtmlElement>,
    val body: List<HtmlElement>,
)
