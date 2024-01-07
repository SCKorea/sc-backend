package kr.galaxyhub.sc.crawler.domain

data class HtmlElement(
    val type: String,
    val attributes: Map<String, String>,
    val children: List<HtmlElement>,
) {
    fun isText() = type == "#text"
}
