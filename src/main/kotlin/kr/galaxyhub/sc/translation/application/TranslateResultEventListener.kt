package kr.galaxyhub.sc.translation.application

import io.github.oshai.kotlinlogging.KotlinLogging
import java.util.UUID
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

private val log = KotlinLogging.logger {}

/**
 * TranslationCommandService.translate 메서드의 트랜잭션이 API 호출보다 늦게 끝나면 예외 발생 가능성 있음.
 * 따라서 log를 error로 발생시킴
 * 해당 로그 파악하면 Retry 전략 구성할 것
 * 혹은 TranslationCommandService.translate에서 TransactionalEventListener를 받는곳에서 WebClient 실행 고려
 */
@Component
class TranslateResultEventListener(
    private val translateProgressionStatusChangeService: TranslateProgressionStatusChangeService,
) {

    @EventListener
    fun translateSuccessEventHandler(event: TranslateSuccessEvent) {
        try {
            translateProgressionStatusChangeService.changeComplete(event.translateProgressionId)
        } catch (e: Exception) {
            log.error { "번역 진행 상황의 상태를 변경 중 예외가 발생했습니다. message=${e.message}" }
        }
    }

    @EventListener
    fun translateFailureEventHandler(event: TranslateFailureEvent) {
        try {
            translateProgressionStatusChangeService.changeFailure(event.translateProgressionId, event.message)
        } catch (e: Exception) {
            log.error { "번역 진행 상황의 상태를 변경 중 예외가 발생했습니다. message=${e.message}" }
        }
    }
}

data class TranslateSuccessEvent(
    val translateProgressionId: UUID,
)

data class TranslateFailureEvent(
    val translateProgressionId: UUID,
    val message: String?,
)

