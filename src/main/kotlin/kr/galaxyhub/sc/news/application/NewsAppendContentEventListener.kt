package kr.galaxyhub.sc.news.application

import io.github.oshai.kotlinlogging.KotlinLogging
import java.util.UUID
import kr.galaxyhub.sc.news.domain.Language
import kr.galaxyhub.sc.news.domain.NewsInformation
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

private val log = KotlinLogging.logger {}

/**
 * TranslationCommandService.translate 메시드 호출 시 News가 이미 생성되어 있으므로 예외 발생 가능성은 없음
 * 하지만 혹시 모를 상황에 error 로그 남김
 */
@Component
class NewsAppendContentEventListener(
    private val newsCommandService: NewsCommandService,
) {

    @EventListener
    fun newsAppendContentEventHandler(event: NewsAppendContentEvent) {
        val (newsId, newsInformation, content, language) = event
        try {
            newsCommandService.appendContent(NewsAppendContentCommand(newsId, newsInformation, content, language))
        } catch (e: Exception) {
            log.error { "뉴스에 컨텐츠를 추가하는 중 예외가 발생했습니다. ${e.message}" }
        }
    }
}

data class NewsAppendContentEvent(
    val newsId: UUID,
    val newsInformation: NewsInformation,
    val content: String,
    val language: Language,
)
