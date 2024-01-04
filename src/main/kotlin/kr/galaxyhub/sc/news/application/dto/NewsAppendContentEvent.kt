package kr.galaxyhub.sc.news.application.dto

import java.util.UUID
import kr.galaxyhub.sc.news.domain.Language
import kr.galaxyhub.sc.news.domain.NewsInformation

data class NewsAppendContentEvent(
    val newsId: UUID,
    val newsInformation: NewsInformation,
    val content: String,
    val language: Language
)
