package kr.galaxyhub.sc.translation.domain

import kr.galaxyhub.sc.news.domain.Content
import kr.galaxyhub.sc.news.domain.Language
import reactor.core.publisher.Mono

interface TranslatorClient {

    fun requestTranslate(content: Content, targetLanguage: Language): Mono<Content>

    fun getProvider(): TranslatorProvider
}
