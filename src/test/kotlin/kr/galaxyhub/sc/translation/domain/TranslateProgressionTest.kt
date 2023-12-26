package kr.galaxyhub.sc.translation.domain

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.throwable.shouldHaveMessage
import java.util.UUID
import kr.galaxyhub.sc.common.exception.BadRequestException
import kr.galaxyhub.sc.news.domain.Language

class TranslateProgressionTest : DescribeSpec({

    describe("constructor") {
        context("번역 진행 상황이 생성되면") {
            val translateProgression = translateProgression()

            it("translationStatus는 PROGRESS이다.") {
                translateProgression.translationStatus shouldBe TranslationStatus.PROGRESS
            }

            it("message는 null이다.") {
                translateProgression.message shouldBe null
            }
        }

        context("sourceLanguage와 targetLanguage가 같으면") {
            it("BadRequestException 예외를 던진다.") {
                val ex = shouldThrow<BadRequestException> {
                    translateProgression(
                        sourceLanguage = Language.ENGLISH,
                        targetLanguage = Language.ENGLISH
                    )
                }
                ex shouldHaveMessage "sourceLanguage와 targetLanguage는 같을 수 없습니다. language=ENGLISH"
            }
        }
    }

    describe("changeComplete") {
        val translateProgression = translateProgression()

        context("메서드를 호출하면") {
            translateProgression.changeComplete()

            it("translationStatus가 COMPLETE로 변경된다.") {
                translateProgression.translationStatus shouldBe TranslationStatus.COMPLETE
            }

            it("message는 변경되지 않는다.") {
                translateProgression.message shouldBe null
            }

            context("translationStatus가 변경된 상태에서 호출하면") {
                it("BadRequestException 예외를 던진다.") {
                    val ex = shouldThrow<BadRequestException> { translateProgression.changeComplete() }
                    ex shouldHaveMessage "translationStatus이 PROGRESS가 아닙니다. translationStatus=COMPLETE"
                }
            }
        }
    }

    describe("changeFailure") {
        val translateProgression = translateProgression()

        context("메서드를 호출하면") {
            translateProgression.changeFailure("예외가 발생했습니다.")

            it("translationStatus가 FAILURE로 변경된다.") {
                translateProgression.translationStatus shouldBe TranslationStatus.FAILURE
            }

            it("message가 변경된다.") {
                translateProgression.message shouldBe "예외가 발생했습니다."
            }

            context("translationStatus가 변경된 상태에서 호출하면") {
                it("BadRequestException 예외를 던진다.") {
                    val ex = shouldThrow<BadRequestException> { translateProgression.changeComplete() }
                    ex shouldHaveMessage "translationStatus이 PROGRESS가 아닙니다. translationStatus=FAILURE"
                }
            }
        }
    }
})

private fun translateProgression(
    newsId: UUID = UUID.randomUUID(),
    sourceLanguage: Language = Language.ENGLISH,
    targetLanguage: Language = Language.KOREAN,
    translatorProvider: TranslatorProvider = TranslatorProvider.LOCAL,
) = TranslateProgression(newsId, sourceLanguage, targetLanguage, translatorProvider)
