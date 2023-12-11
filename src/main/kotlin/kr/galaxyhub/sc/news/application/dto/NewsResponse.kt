package kr.galaxyhub.sc.news.application.dto

import java.time.ZonedDateTime
import java.util.UUID
import kr.galaxyhub.sc.news.domain.Language
import kr.galaxyhub.sc.news.domain.News
import kr.galaxyhub.sc.news.domain.NewsType

data class NewsResponse(
    val id: UUID,
    val newsType: NewsType,
    val title: String?,
    val excerpt: String?,
    val publishedAt: ZonedDateTime,
    val supportLanguages: Set<Language>,
    val originId: Long,
    val originUrl: String,
) {

    companion object {

        fun from(news: News) = NewsResponse(
            id = news.id,
            newsType = news.newsType,
            title = news.newsInformation.title,
            excerpt = news.newsInformation.excerpt,
            publishedAt = news.publishedAt,
            supportLanguages = news.supportLanguages,
            originId = news.originId,
            originUrl = news.originUrl,
        )
    }
}
