package kr.galaxyhub.sc.crawler.infra.parser

import kr.galaxyhub.sc.crawler.domain.HtmlParser
import kr.galaxyhub.sc.crawler.domain.MarkdownLine
import kr.galaxyhub.sc.crawler.domain.MarkdownType
import org.jsoup.Jsoup

class MarkdownHtmlParser : HtmlParser {

    override fun parse(contents: List<String>): String {
        return contentsToMarkdowns(contents).joinToString(System.lineSeparator())
    }

    private fun contentsToMarkdowns(contents: List<String>): List<MarkdownLine> {
        return contents.stream()
            .map { Jsoup.parse(it).body().children() }
            .map { elements -> elements.map { MarkdownLine(MarkdownType.fromTag(it.normalName()), it.text()) } }
            .toList().flatten()
    }
}
