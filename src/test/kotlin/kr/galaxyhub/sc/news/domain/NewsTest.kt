package kr.galaxyhub.sc.news.domain

import io.kotest.assertions.assertSoftly
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.throwable.shouldHaveMessage
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
                assertSoftly {
                    news.newsInformation shouldNotBe null
                    news.newsInformation shouldBe NewsInformation.EMPTY
                }
            }
        }
    }

    describe("addContent") {
        context("뉴스에 컨텐츠가 추가된 상태에서") {
            val news = NewsFixture.create()
            news.addContent(ContentFixture.of(Language.ENGLISH))

            context("중복된 언어의 컨텐츠가 추가되면") {
                it("IllegalArgumentException 예외를 던진다.") {
                    val exception = shouldThrow<IllegalArgumentException> {
                        news.addContent(ContentFixture.of(Language.ENGLISH))
                    }
                    exception shouldHaveMessage "이미 해당 언어로 작성된 뉴스가 있습니다."
                }
            }

            context("중복되지 않은 언어의 컨텐츠가 추가되면") {
                val content = ContentFixture.of(Language.KOREAN)
                news.addContent(content)

                it("성공적으로 추가된다.") {
                    news.supportLanguages shouldContainExactly setOf(Language.KOREAN, Language.ENGLISH)
                }

                it("newsInformation은 변경되지 않는다.") {
                    news.newsInformation shouldNotBe content.newsInformation
                }
            }
        }

        context("뉴스에 컨텐츠가 없는 상태에서") {
            val news = NewsFixture.create()
            context("컨텐츠가 추가되면") {
                val content = ContentFixture.of(Language.ENGLISH)
                news.addContent(content)

                it("성공적으로 추가된다.") {
                    news.supportLanguages shouldContainExactly setOf(Language.ENGLISH)
                }

                it("newsInformation이 설정된다.") {
                    news.newsInformation shouldBe content.newsInformation
                }
            }
        }
    }
})
