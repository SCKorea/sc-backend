package kr.galaxyhub.sc.news.fixture

import kr.galaxyhub.sc.news.domain.Content
import kr.galaxyhub.sc.news.domain.Language
import kr.galaxyhub.sc.news.domain.News
import kr.galaxyhub.sc.news.domain.NewsInformation

object ContentFixture {

    fun create(
        newsInformation: NewsInformation = NewsInformation(
            title = "Star Citizen Live",
            excerpt = "You asked. We're answering! Join us today for a live Q&A show with the Vehicle Gameplay team."
        ),
        language: Language = Language.ENGLISH,
        content: String = "blah blah",
        news: News,
    ): Content {
        return Content(
            newsInformation = newsInformation,
            language = language,
            content = content,
            news = news
        )
    }
}
