package kr.galaxyhub.sc.crawler.domain

import org.jsoup.nodes.Document

fun interface DocumentProvider {

    fun provide(url: String): Document
}
