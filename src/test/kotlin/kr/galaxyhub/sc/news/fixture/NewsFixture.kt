package kr.galaxyhub.sc.news.fixture

import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import kr.galaxyhub.sc.news.domain.News
import kr.galaxyhub.sc.news.domain.NewsType

object NewsFixture {

    fun create(): News = News(
        newsType = NewsType.NEWS,
        publishedAt = ZonedDateTime.of(LocalDateTime.parse("2023-12-04T03:53:33"), ZoneId.systemDefault()),
        originId = 19478,
    )
}
