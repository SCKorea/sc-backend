package kr.galaxyhub.sc.news.application

import io.github.oshai.kotlinlogging.KotlinLogging
import kr.galaxyhub.sc.news.application.dto.NewsAppendContentEvent
import kr.galaxyhub.sc.news.domain.NewsRepository
import kr.galaxyhub.sc.news.domain.getOrThrow
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

private val log = KotlinLogging.logger {}

/**
 * TranslationCommandService.translate 메시드 호출 시 News가 이미 생성되어 있으므로 예외 발생 가능성은 없음
 * 하지만 혹시 모를 상황에 error 로그 남김
 */
@Component
@Transactional
class NewsAppendContentEventListener(
    private val newsRepository: NewsRepository
) {

    @EventListener
    fun appendContent(event: NewsAppendContentEvent) {
        runCatching {
            newsRepository.getOrThrow(event.newsId)
        }.onSuccess {
            it.addContent(event.content)
        }.onFailure {
            log.error { "뉴스에 컨텐츠를 추가하는 중 예외가 발생했습니다. ${it.message}" }
        }
    }
}

