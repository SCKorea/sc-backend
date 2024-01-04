package kr.galaxyhub.sc.translation.application

import io.kotest.assertions.throwables.shouldNotThrow
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.throwable.shouldHaveMessage
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kr.galaxyhub.sc.common.exception.BadRequestException
import kr.galaxyhub.sc.common.exception.NotFoundException
import kr.galaxyhub.sc.news.application.NewsAppendContentEvent
import kr.galaxyhub.sc.news.domain.Language
import kr.galaxyhub.sc.news.domain.MemoryNewsRepository
import kr.galaxyhub.sc.news.domain.NewsInformation
import kr.galaxyhub.sc.news.fixture.ContentFixture
import kr.galaxyhub.sc.news.fixture.NewsFixture
import kr.galaxyhub.sc.translation.domain.MemoryTranslateProgressionRepository
import kr.galaxyhub.sc.translation.domain.TranslatorProvider
import kr.galaxyhub.sc.translation.domain.getOrThrow
import org.springframework.context.ApplicationEventPublisher
import reactor.core.publisher.Mono

class TranslationCommandServiceTest : DescribeSpec({

    val translationProgressionRepository = MemoryTranslateProgressionRepository()
    val newsRepository = MemoryNewsRepository()
    val translatorClient = mockk<TranslatorClient>()
    val eventPublisher = mockk<ApplicationEventPublisher>()
    val translationCommandService = TranslationCommandService(
        translateProgressionRepository = translationProgressionRepository,
        newsRepository = newsRepository,
        translatorClients = TranslatorClients(mapOf(TranslatorProvider.LOCAL to translatorClient)),
        eventPublisher = eventPublisher,
    )

    afterContainer {
        clearAllMocks()
        translationProgressionRepository.clear()
        newsRepository.clear()
    }

    describe("translate") {
        context("뉴스에 번역을 원하는 언어의 컨텐츠가 존재하면") {
            val news = NewsFixture.create()
            news.addContent(ContentFixture.create(newsId = news.id, language = Language.ENGLISH))
            news.addContent(ContentFixture.create(newsId = news.id, language = Language.KOREAN))
            newsRepository.save(news)
            val command = TranslationCommand(news.id, Language.ENGLISH, Language.KOREAN, TranslatorProvider.LOCAL)

            it("BadRequestException 예외를 던진다.") {
                val ex = shouldThrow<BadRequestException> {
                    translationCommandService.translate(command)
                }
                ex shouldHaveMessage "이미 뉴스에 번역된 컨텐츠가 존재합니다. targetLanguage=${command.targetLanguage}"
            }
        }

        context("뉴스에 번역할 원문 언어의 컨텐츠가 없으면") {
            val news = NewsFixture.create()
            news.addContent(ContentFixture.create(newsId = news.id, language = Language.KOREAN))
            val command = TranslationCommand(news.id, Language.ENGLISH, Language.KOREAN, TranslatorProvider.LOCAL)

            it("BadRequestException 예외를 던진다.") {
                val ex = shouldThrow<NotFoundException> {
                    translationCommandService.translate(command)
                }
                ex shouldHaveMessage "식별자와 언어에 대한 뉴스를 찾을 수 없습니다. id=${news.id}, language=${command.sourceLanguage}"
            }
        }

        context("translatorClient에 예외가 발생하면") {
            val news = NewsFixture.create()
            news.addContent(ContentFixture.create(newsId = news.id, language = Language.ENGLISH))
            newsRepository.save(news)
            val command = TranslationCommand(news.id, Language.ENGLISH, Language.KOREAN, TranslatorProvider.LOCAL)
            every { translatorClient.requestTranslate(any()) } returns Mono.fromSupplier {
                throw IllegalArgumentException()
            }
            every { eventPublisher.publishEvent(ofType<TranslateFailureEvent>()) } returns Unit

            val translateProgressionId = translationCommandService.translate(command)

            it("translationProgression가 저장된다.") {
                shouldNotThrow<NotFoundException> {
                    translationProgressionRepository.getOrThrow(translateProgressionId)
                }
            }

            it("TranslatorFailureEvent 이벤트가 발행된다.") {
                verify { eventPublisher.publishEvent(ofType<TranslateFailureEvent>()) }
            }

            it("NewsAppendContentEvent 이벤트는 발행되지 않는다.") {
                verify(exactly = 0) { eventPublisher.publishEvent(ofType<NewsAppendContentEvent>()) }
            }

            it("TranslationSuccessEvent 이벤트는 발행되지 않는다.") {
                verify(exactly = 0) { eventPublisher.publishEvent(ofType<TranslateSuccessEvent>()) }
            }
        }

        context("유효한 요청이 전달되면") {
            val news = NewsFixture.create()
            news.addContent(ContentFixture.create(newsId = news.id, language = Language.ENGLISH))
            newsRepository.save(news)
            val command = TranslationCommand(news.id, Language.ENGLISH, Language.KOREAN, TranslatorProvider.LOCAL)
            val translatorClientResponse = TranslatorClientResponse(NewsInformation("제목", "발췌"), "내용", Language.KOREAN)
            every { translatorClient.requestTranslate(any()) } returns Mono.just(translatorClientResponse)
            every { eventPublisher.publishEvent(ofType<NewsAppendContentEvent>()) } returns Unit
            every { eventPublisher.publishEvent(ofType<TranslateSuccessEvent>()) } returns Unit

            val translateProgressionId = translationCommandService.translate(command)

            it("translationProgression가 저장된다.") {
                shouldNotThrow<NotFoundException> {
                    translationProgressionRepository.getOrThrow(translateProgressionId)
                }
            }

            it("NewsAppendContentEvent 이벤트가 발행된다.") {
                verify { eventPublisher.publishEvent(ofType<NewsAppendContentEvent>()) }
            }

            it("TranslationSuccessEvent 이벤트가 발행된다.") {
                verify { eventPublisher.publishEvent(ofType<TranslateSuccessEvent>()) }
            }

            it("TranslatorFailureEvent 이벤트는 발행되지 않는다.") {
                verify(exactly = 0) { eventPublisher.publishEvent(ofType<TranslateFailureEvent>()) }
            }
        }
    }
})
