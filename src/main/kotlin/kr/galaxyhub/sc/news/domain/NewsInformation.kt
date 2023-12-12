package kr.galaxyhub.sc.news.domain

import jakarta.persistence.Column
import jakarta.persistence.Embeddable

@Embeddable
data class NewsInformation(
    @Column(name = "title", nullable = true)
    val title: String?,

    @Column(name = "excerpt", nullable = true)
    val excerpt: String?,
) {

    companion object {

        val EMPTY = NewsInformation(null, null)
    }
}

