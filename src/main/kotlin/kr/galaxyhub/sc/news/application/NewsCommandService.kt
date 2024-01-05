package kr.galaxyhub.sc.news.application

import java.time.ZonedDateTime
import java.util.UUID
import kr.galaxyhub.sc.news.domain.Content
import kr.galaxyhub.sc.news.domain.Language
import kr.galaxyhub.sc.news.domain.News
import kr.galaxyhub.sc.news.domain.NewsInformation
import kr.galaxyhub.sc.news.domain.NewsRepository
import kr.galaxyhub.sc.news.domain.NewsType
import kr.galaxyhub.sc.news.domain.getFetchByIdAndLanguage
import kr.galaxyhub.sc.news.domain.getOrThrow
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

    fun updateContent(newsId: UUID, command: NewsUpdateCommand) {
        val (language, newsInformation, content) = command
        val news = newsRepository.getFetchByIdAndLanguage(newsId, language)
        val contentByLanguage = news.getContentByLanguage(language)
        newsInformation?.also {
            contentByLanguage.updateNewsInformation(it)
        }
        content?.also {
            contentByLanguage.updateContent(it)
        }
    }

    fun appendContent(command: NewsAppendContentCommand) {
        val news = newsRepository.getOrThrow(command.newsId)
        news.addContent(command.toContent())
    }
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

data class NewsUpdateCommand(
    val language: Language,
    val newsInformation: NewsInformation?,
    val content: String?,
)

data class NewsAppendContentCommand(
    val newsId: UUID,
    val newsInformation: NewsInformation,
    val content: String,
    val language: Language,
) {

    fun toContent() = Content(
        newsId = newsId,
        newsInformation = newsInformation,
        language = language,
        content = content
    )
}
