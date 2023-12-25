package kr.galaxyhub.sc.news.domain

import io.kotest.assertions.assertSoftly
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.throwable.shouldHaveMessage
import kr.galaxyhub.sc.common.exception.BadRequestException
import kr.galaxyhub.sc.news.fixture.ContentFixture
import kr.galaxyhub.sc.news.fixture.NewsFixture

class NewsTest : DescribeSpec({

    describe("constructor") {
        context("뉴스가 생성되면") {
            val news = NewsFixture.create()

            it("supportLanguages는 비어있다.") {
                news.supportLanguages shouldHaveSize 0
            }

            it("newsInformation은 null이 아닌, Empty이다.") {
                val expect = news.newsInformation
                assertSoftly {
                    expect shouldNotBe null
                    expect shouldBe NewsInformation.EMPTY
                }
            }
        }
    }

    describe("addContent") {
        context("컨텐츠의 뉴스가 동일하지 않으면") {
            val news = NewsFixture.create()
            val otherNews = NewsFixture.create()
            val content = ContentFixture.create(language = Language.ENGLISH, news = otherNews)

            it("BadRequestException 예외를 던진다.") {
                val exception = shouldThrow<BadRequestException> {
                    news.addContent(content)
                }
                exception shouldHaveMessage "컨텐츠에 등록된 뉴스가 동일하지 않습니다."
            }
        }

        context("뉴스에 중복된 언어의 컨텐츠를 추가하면") {
            val news = NewsFixture.create()
            val englishContent = ContentFixture.create(language = Language.ENGLISH, news = news)
            val otherEnglishContent = ContentFixture.create(language = Language.ENGLISH, news = news)

            news.addContent(englishContent)

            it("BadRequestException 예외를 던진다.") {
                val exception = shouldThrow<BadRequestException> {
                    news.addContent(otherEnglishContent)
                }
                exception shouldHaveMessage "이미 해당 언어로 작성된 뉴스가 있습니다."
            }
        }

        context("뉴스에 중복되지 않은 언어의 컨텐츠를 추가하면") {
            val news = NewsFixture.create()
            val englishContent = ContentFixture.create(language = Language.ENGLISH, news = news)
            val koreanContent = ContentFixture.create(language = Language.KOREAN, news = news)

            news.addContent(englishContent)
            news.addContent(koreanContent)

            it("supportLanguages가 성공적으로 추가된다.") {
                news.supportLanguages shouldContainExactly setOf(Language.ENGLISH, Language.KOREAN)
            }

            it("처음 추가된 컨텐츠의 newsInformation이 설정된다.") {
                news.newsInformation shouldBe englishContent.newsInformation
            }
        }
    }
})
