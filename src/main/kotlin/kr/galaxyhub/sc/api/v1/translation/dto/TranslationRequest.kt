package kr.galaxyhub.sc.api.v1.translation.dto

import kr.galaxyhub.sc.news.domain.Language

data class TranslationRequest(
    val destinationLanguage: Language
)
