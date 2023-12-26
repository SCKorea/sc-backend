package kr.galaxyhub.sc.api.v1.translation.dto

import java.util.UUID
import kr.galaxyhub.sc.news.domain.Language
import kr.galaxyhub.sc.translation.application.dto.TranslationCommand
import kr.galaxyhub.sc.translation.domain.TranslatorProvider

data class TranslationRequest(
    val sourceLanguage: Language,
    val targetLanguage: Language,
    val translatorProvider: TranslatorProvider,
) {

    fun toCommand(newsId: UUID) = TranslationCommand(
        newsId = newsId,
        sourceLanguage = sourceLanguage,
        targetLanguage = targetLanguage,
        translatorProvider = translatorProvider
    )
}
