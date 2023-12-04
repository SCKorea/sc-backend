package kr.galaxyhub.sc.news.domain

import jakarta.persistence.Column
import jakarta.persistence.Embedded
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.Lob
import jakarta.persistence.ManyToOne

@Entity
class Content(
    newsInformation: NewsInformation,
    language: Language,
    content: String,
) {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val sequence: Long? = null

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "news_id", nullable = false)
    var news: News? = null
        private set

    @Embedded
    var newsInformation: NewsInformation = newsInformation
        private set

    @Enumerated(EnumType.STRING)
    @Column(name = "language", nullable = false)
    var language: Language = language
        private set

    @Lob
    @Column(name = "content", nullable = false)
    var content: String = content
        private set

    fun initialNews(news: News) {
        if (this.news != null) {
            throw IllegalArgumentException("이미 뉴스에 등록된 컨텐츠 입니다.") // TODO 명확한 예외 정의할 것
        }
        this.news = news
    }
}
