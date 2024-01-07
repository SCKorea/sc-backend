package kr.galaxyhub.sc.crawler.infra.provider

import kr.galaxyhub.sc.crawler.domain.*
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import org.jsoup.nodes.Node
import org.jsoup.select.Elements

class JsoupDocumentProvider : DocumentProvider {
    override fun provide(option: DocumentOption): HtmlDocument = createHtmlDocument(
        element = Jsoup.parse(option.url, option.timeout),
        bodyClassNames = option.bodyClassNames
    )

    private fun createHtmlDocument(element: Element, bodyClassNames: List<String>): HtmlDocument = HtmlDocument(
        head = createHtmlElement(element.getElementsByTag("head").toNodes()),
        body = bodyClassNames.map {
            createHtmlElement(element.getElementsByClass(it).toNodes())
        }.flatten()
    )

    private fun createHtmlElement(nodes: List<Node>): List<HtmlElement> = nodes.map { node ->
        HtmlElement(
            type = node.normalName(),
            attributes = node.attributes().associate { Pair(it.key, it.value) },
            children = createHtmlElement(node.childNodes())
        )
    }

    private fun Elements.toNodes(): List<Node> = this.map { it.childNodes() }.flatten()
}
