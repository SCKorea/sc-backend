package kr.galaxyhub.sc.crawler.domain

fun interface DocumentProvider {

    fun provide(option: DocumentOption): HtmlDocument
}
