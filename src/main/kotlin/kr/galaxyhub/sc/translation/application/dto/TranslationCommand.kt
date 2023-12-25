package kr.galaxyhub.sc.translation.application.dto

import java.util.UUID
import kr.galaxyhub.sc.news.domain.Language
import kr.galaxyhub.sc.translation.domain.TranslatorProvider

data class TranslationCommand(
    val newsId: UUID,
    val sourceLanguage: Language,
    val targetLanguage: Language,
    val translatorProvider: TranslatorProvider,
)
