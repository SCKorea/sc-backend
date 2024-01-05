package kr.galaxyhub.sc.translation.application

import kr.galaxyhub.sc.news.domain.Language
import kr.galaxyhub.sc.news.domain.NewsInformation
import kr.galaxyhub.sc.translation.domain.TranslatorProvider
import reactor.core.publisher.Mono

interface TranslatorClient {

    fun requestTranslate(request: TranslatorClientRequest): Mono<TranslatorClientResponse>

    fun getProvider(): TranslatorProvider
}

data class TranslatorClientRequest(
    val newsInformation: NewsInformation,
    val content: String,
    val sourceLanguage: Language,
    val targetLanguage: Language,
)

data class TranslatorClientResponse(
    val newsInformation: NewsInformation,
    val content: String,
    val language: Language
)
