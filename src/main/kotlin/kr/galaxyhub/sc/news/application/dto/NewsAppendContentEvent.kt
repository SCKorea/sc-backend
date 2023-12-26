package kr.galaxyhub.sc.news.application.dto

import java.util.UUID
import kr.galaxyhub.sc.news.domain.Content

data class NewsAppendContentEvent(
    val newsId: UUID,
    val translateProgressionId: UUID,
    val content: Content
)
