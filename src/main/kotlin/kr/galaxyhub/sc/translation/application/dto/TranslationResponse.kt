package kr.galaxyhub.sc.translation.application.dto

import java.util.UUID
import kr.galaxyhub.sc.translation.domain.TranslateProgression
import kr.galaxyhub.sc.translation.domain.TranslationStatus

data class TranslationResponse(
    val translateProgressionId: UUID,
    val targetNewsId: UUID,
    val translationStatus: TranslationStatus,
    val message: String? = null,
) {

    companion object {

        fun from(translationProgression: TranslateProgression): TranslationResponse {
            return TranslationResponse(
                translateProgressionId = translationProgression.id,
                targetNewsId = translationProgression.newsId,
                translationStatus = translationProgression.translationStatus,
                message = translationProgression.message
            )
        }
    }
}
