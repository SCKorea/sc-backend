package kr.galaxyhub.sc.translation.domain

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Table
import jakarta.persistence.UniqueConstraint
import java.util.UUID
import kr.galaxyhub.sc.common.domain.PrimaryKeyEntity
import kr.galaxyhub.sc.common.support.validate
import kr.galaxyhub.sc.news.domain.Language

@Entity
@Table(
    uniqueConstraints = [
        UniqueConstraint(
            name = "UNIQUE_NEWS_ID_AND_DESTINATION_LANGUAGE",
            columnNames = [
                "news_id",
                "target_language"
            ]
        )
    ]
)
class TranslateProgression(
    newsId: UUID,
    sourceLanguage: Language,
    targetLanguage: Language,
    translatorProvider: TranslatorProvider,
) : PrimaryKeyEntity() {

    init {
        validate(sourceLanguage == targetLanguage) {
            "sourceLanguage와 targetLanguage는 같을 수 없습니다. language=$sourceLanguage"
        }
    }

    @Column(name = "news_id", nullable = false, columnDefinition = "uuid")
    val newsId: UUID = newsId

    @Enumerated(EnumType.STRING)
    @Column(name = "source_language", nullable = false, columnDefinition = "varchar")
    val sourceLanguage: Language = sourceLanguage

    @Enumerated(EnumType.STRING)
    @Column(name = "target_language", nullable = false, columnDefinition = "varchar")
    val targetLanguage: Language = targetLanguage

    @Enumerated(EnumType.STRING)
    @Column(name = "translation_status", nullable = false, columnDefinition = "varchar")
    var translationStatus: TranslationStatus = TranslationStatus.PROGRESS
        protected set

    @Enumerated(EnumType.STRING)
    @Column(name = "translator_provider", nullable = false, columnDefinition = "varchar")
    val translatorProvider: TranslatorProvider = translatorProvider

    @Column(name = "message")
    var message: String? = null
        protected set

    fun changeComplete() {
        checkStatusProgress()
        translationStatus = TranslationStatus.COMPLETE
    }

    fun changeFailure(message: String?) {
        checkStatusProgress()
        translationStatus = TranslationStatus.FAILURE
        this.message = message
    }

    private fun checkStatusProgress() {
        validate(translationStatus != TranslationStatus.PROGRESS) {
            "translationStatus이 PROGRESS가 아닙니다. translationStatus=$translationStatus"
        }
    }
}
