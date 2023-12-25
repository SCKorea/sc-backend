package kr.galaxyhub.sc.news.application

import java.time.ZonedDateTime
import java.util.UUID
import kr.galaxyhub.sc.news.domain.Content
import kr.galaxyhub.sc.news.domain.Language
import kr.galaxyhub.sc.news.domain.News
import kr.galaxyhub.sc.news.domain.NewsInformation
import kr.galaxyhub.sc.news.domain.NewsRepository
import kr.galaxyhub.sc.news.domain.NewsType
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class NewsCommandService(
    private val newsRepository: NewsRepository,
) {

    fun create(command: NewsCreateCommand): UUID {
        val news = newsRepository.findByOriginId(command.originId) ?: newsRepository.save(command.toNews())
        val content = createContent(news.id, command)
        news.addContent(content)
        return news.id
    }

    private fun createContent(newsId: UUID, command: NewsCreateCommand) = Content(
        newsId = newsId,
        language = command.language,
        content = command.content,
        newsInformation = NewsInformation(
            title = command.title,
            excerpt = command.excerpt
        ),
    )
}

data class NewsCreateCommand(
    val newsType: NewsType,
    val title: String,
    val excerpt: String?,
    val publishedAt: ZonedDateTime,
    val originId: Long,
    val originUrl: String,
    val language: Language,
    val content: String,
) {

    fun toNews() = News(
        newsType = newsType,
        publishedAt = publishedAt,
        originId = originId,
        originUrl = originUrl
    )
}
