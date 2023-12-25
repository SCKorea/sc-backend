package kr.galaxyhub.sc.news.domain

import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Convert
import jakarta.persistence.Embedded
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.OneToMany
import java.time.ZonedDateTime
import java.util.EnumSet
import kr.galaxyhub.sc.common.domain.PrimaryKeyEntity
import kr.galaxyhub.sc.common.exception.NotFoundException
import kr.galaxyhub.sc.common.support.validate

@Entity
class News(
    newsType: NewsType,
    originId: Long,
    originUrl: String,
    publishedAt: ZonedDateTime,
) : PrimaryKeyEntity() {

    @Enumerated(EnumType.STRING)
    @Column(name = "news_type", nullable = false, columnDefinition = "varchar")
    var newsType: NewsType = newsType
        protected set

    @Column(name = "origin_id", nullable = false)
    var originId: Long = originId
        protected set

    @Column(name = "origin_url", nullable = false)
    var originUrl: String = originUrl
        protected set

    @Column(name = "published_at", nullable = false)
    var publishedAt: ZonedDateTime = publishedAt
        protected set

    @Embedded
    var newsInformation: NewsInformation = NewsInformation.EMPTY
        protected set

    @Convert(converter = EnumSetLanguageConverter::class)
    @Column(name = "support_languages", nullable = false)
    private val mutableSupportLanguages: EnumSet<Language> = EnumSet.noneOf(Language::class.java)
    val supportLanguages: Set<Language> get() = mutableSupportLanguages.toHashSet()

    @OneToMany(fetch = FetchType.LAZY, cascade = [CascadeType.PERSIST], mappedBy = "news")
    private val contents: MutableList<Content> = mutableListOf()

    fun addContent(content: Content) {
        validateAddContent(content)
        if (mutableSupportLanguages.isEmpty()) {
            newsInformation = content.newsInformation
        }
        contents.add(content)
        mutableSupportLanguages.add(content.language)
    }

    private fun validateAddContent(content: Content) {
        validate(content.news != this) { "컨텐츠에 등록된 뉴스가 동일하지 않습니다." }
        validate(mutableSupportLanguages.contains(content.language)) { "이미 해당 언어로 작성된 뉴스가 있습니다." }
    }

    fun getContentByLanguage(language: Language): Content {
        return contents.find { it.language == language }
            ?: throw NotFoundException("해당 언어의 컨텐츠가 존재하지 않습니다.")
    }

    fun isSupportLanguage(language: Language): Boolean {
        return mutableSupportLanguages.contains(language)
    }
}
