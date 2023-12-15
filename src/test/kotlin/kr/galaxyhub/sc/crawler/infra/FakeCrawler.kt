package kr.galaxyhub.sc.crawler.infra

import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import kr.galaxyhub.sc.news.application.Crawler
import kr.galaxyhub.sc.news.application.NewsCreateCommand
import kr.galaxyhub.sc.news.domain.Language
import kr.galaxyhub.sc.news.domain.NewsType

class FakeCrawler(
    private val canCrawl: Boolean
) : Crawler {

    override fun canCrawl(url: String): Boolean {
        return canCrawl
    }

    override fun crawling(url: String): NewsCreateCommand {
        return NewsCreateCommand(
            newsType = NewsType.NEWS,
            title = "제목",
            excerpt = "줄거리",
            publishedAt = ZonedDateTime.of(LocalDateTime.parse("2023-12-04T03:53:33"), ZoneId.systemDefault()),
            originId = 1L,
            originUrl = "https://sc.galaxyhub.kr/",
            language = Language.KOREAN,
            content = "내용"
        )
    }
}
