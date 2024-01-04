package kr.galaxyhub.sc.translation.application

import io.github.oshai.kotlinlogging.KotlinLogging
import java.util.UUID
import kr.galaxyhub.sc.common.support.validate
import kr.galaxyhub.sc.news.application.NewsAppendContentEvent
import kr.galaxyhub.sc.news.domain.Content
import kr.galaxyhub.sc.news.domain.Language
import kr.galaxyhub.sc.news.domain.NewsRepository
import kr.galaxyhub.sc.news.domain.getFetchByIdAndLanguage
import kr.galaxyhub.sc.translation.domain.TranslateProgression
import kr.galaxyhub.sc.translation.domain.TranslateProgressionRepository
import kr.galaxyhub.sc.translation.domain.TranslatorProvider
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

private val log = KotlinLogging.logger {}

@Service
@Transactional
class TranslationCommandService(
    private val translateProgressionRepository: TranslateProgressionRepository,
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
        translateProgressionRepository.save(translateProgression)

        val translatorClient = translatorClients.getClient(translatorProvider)
        translatorClient.requestTranslate(content.toRequest(targetLanguage))
            .doOnError {
                log.info { "뉴스 번역 요청이 실패하였습니다. newsId=${newsId}, translateProgressionId=${translateProgression.id} cause=${it.message}" }
                eventPublisher.publishEvent(TranslateFailureEvent(translateProgression.id, it.message))
            }
            .subscribe {
                log.info { "뉴스 번역 요청이 완료되었습니다. newsId=${newsId}, translateProgressionId=${translateProgression.id}" }
                eventPublisher.publishEvent(NewsAppendContentEvent(newsId, it.newsInformation, it.content, it.language))
                eventPublisher.publishEvent(TranslateSuccessEvent(translateProgression.id))
            }
        return translateProgression.id
    }

    private fun Content.toRequest(targetLanguage: Language): TranslatorClientRequest {
        return TranslatorClientRequest(
            newsInformation = newsInformation,
            content = content,
            sourceLanguage = language,
            targetLanguage = targetLanguage
        )
    }
}

data class TranslationCommand(
    val newsId: UUID,
    val sourceLanguage: Language,
    val targetLanguage: Language,
    val translatorProvider: TranslatorProvider,
)

