package kr.galaxyhub.sc.news.domain

import jakarta.persistence.Column
import jakarta.persistence.Embedded
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import java.util.UUID

@Entity
class Content(
    newsId: UUID,
    newsInformation: NewsInformation,
    language: Language,
    content: String,
) {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val sequence: Long? = null

    @JoinColumn(name = "news_id", nullable = false)
    var newsId: UUID = newsId
        protected set

    @Embedded
    var newsInformation: NewsInformation = newsInformation
        protected set

    @Enumerated(EnumType.STRING)
    @Column(name = "language", nullable = false, columnDefinition = "varchar")
    var language: Language = language
        protected set

    @Column(name = "content", nullable = false)
    var content: String = content
        protected set
}
