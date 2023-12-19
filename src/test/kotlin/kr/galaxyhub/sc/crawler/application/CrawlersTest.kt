package kr.galaxyhub.sc.crawler.application

import io.kotest.assertions.throwables.shouldNotThrow
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import kr.galaxyhub.sc.common.exception.BadRequestException
import kr.galaxyhub.sc.crawler.infra.FakeCrawler

class CrawlersTest : DescribeSpec({
    var crawlers: Crawlers

    describe("crawling") {
        context("크롤링이 지원되는 URL이면") {
            crawlers = Crawlers(listOf(FakeCrawler(true)))

            it("예외가 발생하지 않는다.") {
                shouldNotThrow<BadRequestException> {
                    crawlers.crawling("https://news.com/12345-blah-blah")
                }
            }
        }

        context("크롤링이 지원되지 않는 URL이면") {
            crawlers = Crawlers(listOf(FakeCrawler(false)))

            it("예외가 발생한다.") {
                shouldThrow<BadRequestException> {
                    crawlers.crawling("https://news.com/12345-blah-blah")
                }
            }
        }
    }
})
