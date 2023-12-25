package kr.galaxyhub.sc.translation.application.dto

import java.util.UUID

data class TranslatorFailureEvent(
    val translateProgressionId: UUID,
    val message: String?
)
