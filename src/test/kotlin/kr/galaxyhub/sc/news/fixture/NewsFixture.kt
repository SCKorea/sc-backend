package kr.galaxyhub.sc.news.fixture

import java.time.LocalDateTime
import kr.galaxyhub.sc.news.domain.News
import kr.galaxyhub.sc.news.domain.NewsType

object NewsFixture {

    fun create(): News = News(
        newsType = NewsType.NEWS,
        publishedAt = LocalDateTime.parse("2023-12-04T03:53:33"),
        originId = 19478,
    )
}
