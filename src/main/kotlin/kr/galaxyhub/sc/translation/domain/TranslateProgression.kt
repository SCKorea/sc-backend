package kr.galaxyhub.sc.translation.domain

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Table
import jakarta.persistence.UniqueConstraint
import java.util.UUID
import kr.galaxyhub.sc.common.domain.PrimaryKeyEntity
import kr.galaxyhub.sc.news.domain.Language

@Entity
@Table(
    uniqueConstraints = [
        UniqueConstraint(
            name = "UNIQUE_NEWS_ID_AND_DESTINATION_LANGUAGE",
            columnNames = [
                "news_id",
                "destination_language"
            ]
        )
    ]
)
class TranslateProgression(
    newsId: UUID,
    destinationLanguage: Language,
) : PrimaryKeyEntity() {

    @Column(name = "news_id", nullable = false, columnDefinition = "uuid")
    val newsId: UUID = newsId

    @Enumerated(EnumType.STRING)
    @Column(name = "destination_language", nullable = false, columnDefinition = "varchar")
    val destinationLanguage: Language = destinationLanguage

    @Enumerated(EnumType.STRING)
    @Column(name = "translation_status", nullable = false, columnDefinition = "varchar")
    var translationStatus: TranslationStatus = TranslationStatus.PROGRESS
        protected set

    @Column(name = "message")
    var message: String? = null
        protected set
}
