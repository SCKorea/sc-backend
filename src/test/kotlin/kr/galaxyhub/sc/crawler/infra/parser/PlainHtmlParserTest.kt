package kr.galaxyhub.sc.crawler.infra.parser

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe

class PlainHtmlParserTest : DescribeSpec({
    val htmlParser = PlainHtmlParser()

    describe("parse") {
        context("HTML 형식의 문자열 리스트가 주어지면") {
            val htmls = listOf(
                "<h1>제목입니다.</h1>",
                "<h2>소제목입니다.</h2>",
                "<p>내용입니다.</p>"
            )

            it("평문으로 변환된다.") {
                val expect = """
                    제목입니다.
                    소제목입니다.
                    내용입니다.
                    """.trimIndent()

                htmlParser.parse(htmls) shouldBe expect
            }
        }

        context("중첩된 HTML 태그 형식이 주어지면") {
            val htmls = listOf("<p><strong>큰제목</strong>입니다.</p>")

            it("중첩된 태그는 무시된다.") {
                htmlParser.parse(htmls) shouldBe "큰제목입니다."
            }
        }
    }
})
