package kr.galaxyhub.sc.api.v1.news.dto

import kr.galaxyhub.sc.news.application.NewsUpdateCommand
import kr.galaxyhub.sc.news.domain.Language
import kr.galaxyhub.sc.news.domain.NewsInformation

data class NewsUpdateRequest(
    val language: Language,
    val title: String?,
    val excerpt: String?,
    val content: String?,
) {

    fun toCommand(): NewsUpdateCommand {
        return NewsUpdateCommand(
            language = language,
            newsInformation = title?.let {
                NewsInformation(title, excerpt)
            },
            content = content
        )
    }
}
