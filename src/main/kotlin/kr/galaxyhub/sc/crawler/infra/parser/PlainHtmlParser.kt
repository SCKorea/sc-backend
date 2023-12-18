package kr.galaxyhub.sc.crawler.infra.parser

import kr.galaxyhub.sc.crawler.domain.HtmlParser
import org.jsoup.Jsoup

class PlainHtmlParser: HtmlParser {

    override fun parse(contents: List<String>): String {
        return contentsToPlainTexts(contents).joinToString(System.lineSeparator())
    }

    private fun contentsToPlainTexts(contents: List<String>): List<String> {
        return contents.stream()
            .map { Jsoup.parse(it).body().children() }
            .map { elements -> elements.map { it.text() } }
            .toList().flatten()
    }
}
