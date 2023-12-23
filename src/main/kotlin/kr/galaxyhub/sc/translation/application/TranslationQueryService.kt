package kr.galaxyhub.sc.translation.application

import java.util.UUID
import kr.galaxyhub.sc.translation.application.dto.TranslationResponse
import kr.galaxyhub.sc.translation.domain.TranslationProgressionRepository
import kr.galaxyhub.sc.translation.domain.getOrThrow
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class TranslationQueryService(
    private val translationProgressionRepository: TranslationProgressionRepository,
) {

    fun findById(translateProgressionId: UUID): TranslationResponse {
        return translationProgressionRepository.getOrThrow(translateProgressionId)
            .let { TranslationResponse.from(it) }
    }
}
