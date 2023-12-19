package kr.galaxyhub.sc.crawler.application

import java.util.UUID
import kr.galaxyhub.sc.news.application.NewsCommandService
import org.springframework.stereotype.Service

/**
 * 외부 API를 호출하는 서비스이기 때문에 Transcational 어노테이션을 사용하지 않습니다.
 *
 * TODO: Async로 동작하게 만들어서, 블로킹이 되지 않도록 합니다.
 */
@Service
class CrawlerCommandService(
    private val crawlers: Crawlers,
    private val newsCommandService: NewsCommandService,
) {

    fun crawling(url: String): UUID {
        val newsCreateCommand = crawlers.crawling(url)
        return newsCommandService.create(newsCreateCommand)
    }
}

