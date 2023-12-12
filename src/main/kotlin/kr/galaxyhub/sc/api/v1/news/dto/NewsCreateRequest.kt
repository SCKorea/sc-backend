package kr.galaxyhub.sc.api.v1.news.dto

import java.time.ZonedDateTime
import kr.galaxyhub.sc.news.application.NewsCreateCommand
import kr.galaxyhub.sc.news.domain.Language
import kr.galaxyhub.sc.news.domain.NewsType

data class NewsCreateRequest(
    val newsType: NewsType,
    val title: String,
    val excerpt: String?,
    val publishedAt: ZonedDateTime,
    val originId: Long,
    val originUrl: String,
    val language: Language,
    val content: String,
) {

    fun toCommand() = NewsCreateCommand(
        newsType = newsType,
        title = title,
        excerpt = excerpt,
        publishedAt = publishedAt,
        originId = originId,
        originUrl = originUrl,
        language = language,
        content = content
    )
}
