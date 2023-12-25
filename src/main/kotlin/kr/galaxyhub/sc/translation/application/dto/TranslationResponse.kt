package kr.galaxyhub.sc.translation.application.dto

import java.util.UUID
import kr.galaxyhub.sc.news.domain.Language
import kr.galaxyhub.sc.translation.domain.TranslateProgression
import kr.galaxyhub.sc.translation.domain.TranslationStatus
import kr.galaxyhub.sc.translation.domain.TranslatorProvider

data class TranslationResponse(
    val translateProgressionId: UUID,
    val targetNewsId: UUID,
    val sourceLanguage: Language,
    val targetLanguage: Language,
    val translationProvider: TranslatorProvider,
    val translationStatus: TranslationStatus,
    val message: String? = null,
) {

    companion object {

        fun from(translationProgression: TranslateProgression): TranslationResponse {
            return TranslationResponse(
                translateProgressionId = translationProgression.id,
                targetNewsId = translationProgression.newsId,
                translationStatus = translationProgression.translationStatus,
                message = translationProgression.message,
                sourceLanguage = translationProgression.sourceLanguage,
                targetLanguage = translationProgression.targetLanguage,
                translationProvider = translationProgression.translatorProvider
            )
        }
    }
}
