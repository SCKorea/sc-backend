package kr.galaxyhub.sc.news.domain

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.throwable.shouldHaveMessage
import kr.galaxyhub.sc.news.fixture.ContentFixture
import kr.galaxyhub.sc.news.fixture.NewsFixture

class ContentTest : DescribeSpec({

    describe("constructor") {
        context("컨텐츠가 생성되면") {
            val content = ContentFixture.of(Language.ENGLISH)

            it("뉴스는 null이다.") {
                content.news shouldBe null
            }
        }
    }

    describe("initialNews") {
        context("컨텐츠가 뉴스에 등록된 상태이면") {
            val content = ContentFixture.of(Language.ENGLISH)
            content.initialNews(NewsFixture.create())

            it("IllegalArgumentException 예외를 던진다.") {
                val exception = shouldThrow<IllegalArgumentException> {
                    content.initialNews(NewsFixture.create())
                }
                exception shouldHaveMessage "이미 뉴스에 등록된 컨텐츠 입니다."
            }
        }

        context("컨텐츠가 뉴스에 등록된 상태가 아니면") {
            val news = NewsFixture.create()
            val content = ContentFixture.of(Language.ENGLISH)

            content.initialNews(news)

            it("뉴스가 성공적으로 등록된다.") {
                content.news shouldBe news
            }
        }
    }
})
