package kr.galaxyhub.sc.translation.application

import java.util.UUID
import kr.galaxyhub.sc.translation.domain.TranslateProgressionRepository
import kr.galaxyhub.sc.translation.domain.getOrThrow
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class TranslateProgressionStatusChangeService(
    private val translateProgressionRepository: TranslateProgressionRepository,
) {

    fun changeComplete(translateProgressionId: UUID) {
        val translateProgression = translateProgressionRepository.getOrThrow(translateProgressionId)
        translateProgression.changeComplete()
    }

    fun changeFailure(translateProgressionId: UUID, message: String?) {
        val translateProgression = translateProgressionRepository.getOrThrow(translateProgressionId)
        translateProgression.changeFailure(message)
    }
}
