package kr.galaxyhub.sc.crawler.application

import kr.galaxyhub.sc.common.exception.BadRequestException
import kr.galaxyhub.sc.news.application.NewsCreateCommand

class Crawlers(
    private val crawlers: List<Crawler>,
) {

    fun crawling(url: String): NewsCreateCommand {
        for (crawler in crawlers) {
            if (crawler.canCrawl(url)) {
                return crawler.crawling(url)
            }
        }
        throw BadRequestException("지원되지 않는 URL 입니다.")
    }
}
