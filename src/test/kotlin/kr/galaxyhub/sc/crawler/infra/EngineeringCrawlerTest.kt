package kr.galaxyhub.sc.crawler.infra

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.inspectors.forAll
import io.kotest.matchers.shouldBe
import io.kotest.matchers.throwable.shouldHaveMessage
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import kr.galaxyhub.sc.common.exception.BadRequestException
import kr.galaxyhub.sc.crawler.domain.DocumentProvider
import kr.galaxyhub.sc.crawler.infra.parser.MarkdownHtmlParser
import kr.galaxyhub.sc.crawler.infra.parser.PlainHtmlParser
import org.jsoup.Jsoup
import org.jsoup.select.Elements
import org.springframework.core.io.ClassPathResource

class EngineeringCrawlerTest : DescribeSpec({
    val documentProvider = mockk<DocumentProvider>()
    val crawler = EngineeringCrawler(
        objectMapper = jacksonObjectMapper(),
        documentProvider = documentProvider,
        contentParser = MarkdownHtmlParser(),
        introductionParser = PlainHtmlParser()
    )

    afterContainer {
        clearAllMocks()
    }

    describe("canCrawl") {
        context("올바른 URL이 주어지면") {
            val url = "https://robertsspaceindustries.com/comm-link/engineering/12345-blah-blah"

            it("참이 반환된다.") {
                crawler.canCrawl(url) shouldBe true
            }
        }

        context("URL이 /로 끝나지 않으면") {
            val url = "https://robertsspaceindustries.com/comm-link/engineering"

            it("거짓이 반환된다.") {
                crawler.canCrawl(url) shouldBe false
            }
        }

        context("올바르지 않은 URL이 주어지면") {
            val url = "올바르지 않은 URL"

            it("거짓이 반환된다.") {
                crawler.canCrawl(url) shouldBe false
            }
        }

        context("comm-link가 engineering이 아니면") {
            val url = "https://robertsspaceindustries.com/comm-link/spectrum-dispatch/12345-blah-blah"

            it("거짓이 반환된다.") {
                crawler.canCrawl(url) shouldBe false
            }
        }
    }

    describe("crawling") {
        val document = spyk(Jsoup.parse(ClassPathResource("/htmlsource/commlink/engineering.html").file))
        every { documentProvider.provide(any()) } returns document

        context("URL에 식별자가 없거나 올바르지 않으면") {
            val urls = listOf(
                "https://robertsspaceindustries.com/comm-link/engineering/",
                "https://robertsspaceindustries.com/comm-link/engineering/-",
                "https://robertsspaceindustries.com/comm-link/engineering/--",
                "https://robertsspaceindustries.com/comm-link/engineering/number",
            )

            it("BadRequestException 예외를 던진다.") {
                urls.forAll {
                    val exception = shouldThrow<BadRequestException> {
                        crawler.crawling(it)
                    }
                    exception shouldHaveMessage "크롤링할 URL에 식별자가 없거나 올바르지 않습니다."
                }
            }
        }

        context("g-narrative-group 태그를 찾을 수 없으면") {
            every { document.getElementsByTag("g-narrative-group") } returns Elements()

            it("BadRequestException 예외를 던진다.") {
                val exception = shouldThrow<BadRequestException> {
                    crawler.crawling("https://robertsspaceindustries.com/comm-link/engineering/123-blah")
                }
                exception shouldHaveMessage "크롤링한 뉴스에 g-narrative-group 태그가 없습니다."
            }
        }

        context("g-introduction 태그를 찾을 수 없으면") {
            every { document.getElementsByTag("g-introduction") } returns Elements()

            it("BadRequestException 예외를 던진다.") {
                val exception = shouldThrow<BadRequestException> {
                    crawler.crawling("https://robertsspaceindustries.com/comm-link/engineering/123-blah")
                }
                exception shouldHaveMessage "크롤링한 뉴스에 g-introduction 태그가 없습니다."
            }
        }

        context("올바른 URL이 주어지면") {
            val url = "https://robertsspaceindustries.com/comm-link/engineering/123-blah"

            it("Command가 반환된다.") {
                val command = crawler.crawling(url)

                command.originId shouldBe 123
            }
        }
    }
}) {

    override fun isolationMode(): IsolationMode = IsolationMode.InstancePerLeaf
}
