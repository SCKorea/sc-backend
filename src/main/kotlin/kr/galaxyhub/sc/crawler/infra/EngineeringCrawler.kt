package kr.galaxyhub.sc.crawler.infra

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import java.time.ZoneId
import java.time.ZonedDateTime
import kr.galaxyhub.sc.common.exception.BadRequestException
import kr.galaxyhub.sc.crawler.application.Crawler
import kr.galaxyhub.sc.crawler.domain.DocumentProvider
import kr.galaxyhub.sc.crawler.domain.HtmlParser
import kr.galaxyhub.sc.crawler.domain.Introduction
import kr.galaxyhub.sc.news.application.NewsCreateCommand
import kr.galaxyhub.sc.news.domain.Language
import kr.galaxyhub.sc.news.domain.NewsType
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element

/**
 * 2023-12-15 기준
 *
 * https://robertsspaceindustries.com/comm-link/engineering에서 가져오는 HTML 내용에는 단순하게 내부 태그 없는 형식으로 되어 있습니다.
 *
 * 글의 제목과 요약은 g-introduction 태그의 :info 속성으로 나타납니다.
 *
 * 글의 내용은 g-narrative-group 태그안의 g-article 태그의 body 속성으로 나타납니다.
 */
open class EngineeringCrawler(
    private val objectMapper: ObjectMapper,
    private val documentProvider: DocumentProvider,
    private val contentParser: HtmlParser,
    private val introductionParser: HtmlParser
) : Crawler {

    override fun canCrawl(url: String): Boolean {
        return url.startsWith(BASE_URL)
    }

    override fun crawling(url: String): NewsCreateCommand {
        val id = getIdFromUrl(url)
        val document = documentProvider.provide("https://robertsspaceindustries.com/comm-link/SCW/${id}-IMPORT")
        val introduction = getIntroduction(document)
        val narrativeGroup = getNarrativeGroup(document)
        val articles = getArticles(narrativeGroup)

        return NewsCreateCommand(
            newsType = NewsType.NEWS,
            title = introduction.title,
            excerpt = introductionParser.parse(introduction.summary),
            publishedAt = ZonedDateTime.now(ZoneId.systemDefault()),
            originId = id,
            originUrl = url,
            language = Language.ENGLISH,
            content = contentParser.parse(articles)
        )
    }

    private fun getIdFromUrl(url: String): Long {
        return url.substring(BASE_URL.length).substringBefore("-").toLongOrNull()
            ?: throw BadRequestException("크롤링할 URL에 식별자가 없거나 올바르지 않습니다.")
    }

    private fun getIntroduction(document: Document): Introduction {
        val introduction = document.getElementsByTag("g-introduction").first()
            ?: throw BadRequestException("크롤링한 뉴스에 g-introduction 태그가 없습니다.")
        return objectMapper.readValue(introduction.attr(":info"))
    }

    private fun getNarrativeGroup(document: Document): Element {
        return document.getElementsByTag("g-narrative-group").first()
            ?: throw BadRequestException("크롤링한 뉴스에 g-narrative-group 태그가 없습니다.")
    }

    private fun getArticles(narrativeGroup: Element): List<String> {
        return narrativeGroup.stream()
            .filter { it.tagName() == "g-article" }
            .map { it.attr("body") }
            .toList()
    }

    companion object {

        private const val BASE_URL = "https://robertsspaceindustries.com/comm-link/engineering/"
    }
}
