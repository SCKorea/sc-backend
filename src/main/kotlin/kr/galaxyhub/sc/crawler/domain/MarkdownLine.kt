package kr.galaxyhub.sc.crawler.domain

class MarkdownLine(
    private val markdownType: MarkdownType,
    private val content: String,
) {

    override fun toString(): String {
        return "${markdownType.element}$content"
    }
}
