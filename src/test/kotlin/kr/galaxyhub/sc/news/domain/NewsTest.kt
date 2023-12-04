package kr.galaxyhub.sc.news.domain

import io.kotest.assertions.assertSoftly
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.throwable.shouldHaveMessage
import io.mockk.every
import io.mockk.spyk
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
        context("컨텐츠의 뉴스가 null이면") {
            val news = NewsFixture.create()
            val content = spyk(ContentFixture.of(Language.ENGLISH))

            every { content.news } returns null

            it("IllegalArgumentException 예외를 던진다.") {
                val exception = shouldThrow<IllegalArgumentException> {
                    news.addContent(content)
                }
                exception shouldHaveMessage "추가할 컨텐츠에 뉴스가 등록되어 있지 않습니다."
            }
        }

        context("컨텐츠의 뉴스가 null이 아니고") {
            context("컨텐츠의 뉴스가 동일하지 않으면") {
                val news = NewsFixture.create()
                val otherNews = NewsFixture.create()
                val content = spyk(ContentFixture.of(Language.ENGLISH))

                every { content.news } returns otherNews

                it("IllegalArgumentException 예외를 던진다.") {
                    val exception = shouldThrow<IllegalArgumentException> {
                        news.addContent(content)
                    }
                    exception shouldHaveMessage "컨텐츠에 등록된 뉴스가 동일하지 않습니다."
                }
            }

            context("뉴스에 중복된 언어의 컨텐츠를 추가하면") {
                val news = NewsFixture.create()
                val englishContent = spyk(ContentFixture.of(Language.ENGLISH))
                val otherEnglishContent = spyk(ContentFixture.of(Language.ENGLISH))

                every { englishContent.news } returns news
                every { otherEnglishContent.news } returns news

                news.addContent(englishContent)

                it("IllegalArgumentException 예외를 던진다.") {
                    val exception = shouldThrow<IllegalArgumentException> {
                        news.addContent(otherEnglishContent)
                    }
                    exception shouldHaveMessage "이미 해당 언어로 작성된 뉴스가 있습니다."
                }
            }

            context("뉴스에 중복되지 않은 언어의 컨텐츠를 추가하면") {
                val news = NewsFixture.create()
                val englishContent = spyk(ContentFixture.of(Language.ENGLISH))
                val koreanContent = spyk(ContentFixture.of(Language.KOREAN))

                every { englishContent.news } returns news
                every { koreanContent.news } returns news

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
    }
})
