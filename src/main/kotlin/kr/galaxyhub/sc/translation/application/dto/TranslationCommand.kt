package kr.galaxyhub.sc.translation.application.dto

import java.util.UUID
import kr.galaxyhub.sc.news.domain.Language

data class TranslationCommand(
    val newsId: UUID,
    val destinationLanguage: Language
)
