package kr.galaxyhub.sc.crawler.domain

enum class MarkdownType(
    val element: String,
) {

    H1("# "),
    H2("## "),
    H3("### "),
    H4("#### "),
    P(""),
    BR("<BR>")
    ;

    companion object {

        fun fromTag(tag: String): MarkdownType {
            return when (tag) {
                "h1" -> H1
                "h2" -> H2
                "h3" -> H3
                "h4" -> H4
                "br" -> BR
                else -> P
            }
        }
    }
}
