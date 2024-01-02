package kr.galaxyhub.sc.translation.application

import io.github.oshai.kotlinlogging.KotlinLogging
import java.util.UUID
import kr.galaxyhub.sc.common.support.validate
import kr.galaxyhub.sc.news.application.dto.NewsAppendContentEvent
import kr.galaxyhub.sc.news.domain.NewsRepository
import kr.galaxyhub.sc.news.domain.getFetchByIdAndLanguage
import kr.galaxyhub.sc.translation.application.dto.TranslationCommand
import kr.galaxyhub.sc.translation.application.dto.TranslatorFailureEvent
import kr.galaxyhub.sc.translation.domain.TranslateProgression
import kr.galaxyhub.sc.translation.domain.TranslationProgressionRepository
import kr.galaxyhub.sc.translation.domain.TranslatorClients
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

private val log = KotlinLogging.logger {}

@Service
@Transactional
class TranslationCommandService(
    private val translationProgressionRepository: TranslationProgressionRepository,
    private val translatorClients: TranslatorClients,
    private val newsRepository: NewsRepository,
    private val eventPublisher: ApplicationEventPublisher,
) {

    fun translate(command: TranslationCommand): UUID {
        val (newsId, sourceLanguage, targetLanguage, translatorProvider) = command
        val news = newsRepository.getFetchByIdAndLanguage(newsId, sourceLanguage)
        validate(news.isSupportLanguage(targetLanguage)) { "이미 뉴스에 번역된 컨텐츠가 존재합니다. targetLanguage=$targetLanguage" }
        val content = news.getContentByLanguage(sourceLanguage)
        val translateProgression = TranslateProgression(newsId, sourceLanguage, targetLanguage, translatorProvider)
        translationProgressionRepository.save(translateProgression)
        val translatorClient = translatorClients.getClient(translatorProvider)
        translatorClient.requestTranslate(content, targetLanguage)
            .doOnError {
                log.info { "뉴스 번역 요청이 실패하였습니다. newsId=${newsId}, translateProgressionId=${translateProgression.id} cause=${it.message}" }
                eventPublisher.publishEvent(TranslatorFailureEvent(translateProgression.id, it.message))
            }
            .subscribe {
                log.info { "뉴스 번역 요청이 완료되었습니다. newsId=${newsId}, translateProgressionId=${translateProgression.id}" }
                eventPublisher.publishEvent(NewsAppendContentEvent(newsId, translateProgression.id, it))
            }
        return translateProgression.id
    }
}
