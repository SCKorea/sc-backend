package kr.galaxyhub.sc.news.application.dto

import java.time.ZonedDateTime
import java.util.UUID
import kr.galaxyhub.sc.news.domain.Content
import kr.galaxyhub.sc.news.domain.Language
import kr.galaxyhub.sc.news.domain.News
import kr.galaxyhub.sc.news.domain.NewsType

data class NewsDetailResponse(
    val id: UUID,
    val newsType: NewsType,
    val title: String?,
    val excerpt: String?,
    val publishedAt: ZonedDateTime,
    val originId: Long,
    val originUrl: String,
    val language: Language,
    val content: String,
    val supportLanguages: Set<Language>,
) {

    companion object {

        fun of(news: News, content: Content) = NewsDetailResponse(
            id = news.id,
            newsType = news.newsType,
            publishedAt = news.publishedAt,
            supportLanguages = news.supportLanguages,
            originId = news.originId,
            originUrl = news.originUrl,
            title = content.newsInformation.title,
            excerpt = content.newsInformation.excerpt,
            language = content.language,
            content = content.content,
        )
    }
}
