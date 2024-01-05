package kr.galaxyhub.sc.news.fixture

import java.util.UUID
import kr.galaxyhub.sc.news.domain.Language
import kr.galaxyhub.sc.translation.domain.TranslateProgression
import kr.galaxyhub.sc.translation.domain.TranslatorProvider

object TranslateProgressionFixture {

    fun create(
        newsId: UUID = UUID.randomUUID(),
        sourceLanguage: Language = Language.ENGLISH,
        targetLanguage: Language = Language.KOREAN,
        translatorProvider: TranslatorProvider = TranslatorProvider.LOCAL,
    ): TranslateProgression = TranslateProgression(
        newsId = newsId,
        sourceLanguage = sourceLanguage,
        targetLanguage = targetLanguage,
        translatorProvider = translatorProvider
    )
}
