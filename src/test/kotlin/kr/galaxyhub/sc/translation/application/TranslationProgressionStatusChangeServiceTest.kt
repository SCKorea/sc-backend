package kr.galaxyhub.sc.translation.application

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import kr.galaxyhub.sc.news.fixture.TranslateProgressionFixture
import kr.galaxyhub.sc.translation.domain.MemoryTranslateProgressionRepository
import kr.galaxyhub.sc.translation.domain.TranslateProgression
import kr.galaxyhub.sc.translation.domain.TranslationStatus

class TranslationProgressionStatusChangeServiceTest(
    private val translationProgressionRepository: MemoryTranslateProgressionRepository = MemoryTranslateProgressionRepository(),
    private val translateProgressionStatusChangeService: TranslateProgressionStatusChangeService =
        TranslateProgressionStatusChangeService(translationProgressionRepository),
) : DescribeSpec({

    lateinit var translateProgression: TranslateProgression

    beforeContainer {
        translateProgression = TranslateProgressionFixture.create()
        translationProgressionRepository.save(translateProgression)
    }

    afterContainer {
        translationProgressionRepository.clear()
    }

    describe("changeComplete") {
        context("유효한 인자로 메서드를 호출하면") {
            translateProgressionStatusChangeService.changeComplete(translateProgression.id)

            it("translateProgression의 translationStatus를 COMPLETE로 변경한다.") {
                translateProgression.translationStatus shouldBe TranslationStatus.COMPLETE
            }
        }
    }

    describe("changeFailure") {
        context("유효한 인자로 메서드를 호출하면") {
            translateProgressionStatusChangeService.changeFailure(translateProgression.id, "예외 발생")

            it("translateProgression의 message를 설정하고, translationStatus를 FAILURE로 변경한다.") {
                translateProgression.translationStatus shouldBe TranslationStatus.FAILURE
                translateProgression.message shouldBe "예외 발생"
            }
        }
    }
})
