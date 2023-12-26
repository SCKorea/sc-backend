package kr.galaxyhub.sc.translation.infra

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.kotest.assertions.assertSoftly
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.throwable.shouldHaveMessage
import kr.galaxyhub.sc.common.exception.BadRequestException
import kr.galaxyhub.sc.common.exception.InternalServerError
import kr.galaxyhub.sc.common.support.enqueue
import kr.galaxyhub.sc.news.domain.Language
import kr.galaxyhub.sc.news.domain.NewsInformation
import kr.galaxyhub.sc.news.fixture.ContentFixture
import okhttp3.mockwebserver.MockWebServer
import org.springframework.web.reactive.function.client.WebClient

class DeepLTranslatorClientTest : DescribeSpec({

    isolationMode = IsolationMode.InstancePerLeaf

    val objectMapper = jacksonObjectMapper()
    val mockWebServer = MockWebServer()
    val deepLTranslatorClient = DeepLTranslatorClient(
        webClient = WebClient.builder()
            .baseUrl("${mockWebServer.url("/")}")
            .build()
    )

    describe("requestTranslate") {
        context("외부 서버가 200 응답을 반환하면") {
            val response = DeepLResponse(
                listOf(
                    DeepLSentenceResponse(
                        detectedSourceLanguage = "EN",
                        text = "제목입니다."
                    ),
                    DeepLSentenceResponse(
                        detectedSourceLanguage = "EN",
                        text = "발췌입니다."
                    ),
                    DeepLSentenceResponse(
                        detectedSourceLanguage = "EN",
                        text = "내용입니다."
                    )
                )
            )
            mockWebServer.enqueue {
                statusCode(200)
                body(objectMapper.writeValueAsString(response))
            }

            val expect = deepLTranslatorClient.requestTranslate(ContentFixture.create(), Language.KOREAN).block()!!

            it("번역된 응답이 반환된다.") {
                assertSoftly {
                    expect.newsInformation shouldBe NewsInformation("제목입니다.", "발췌입니다.")
                    expect.content shouldBe "내용입니다."
                }
            }
        }

        context("외부 서버가 429 응답을 반환하면") {
            mockWebServer.enqueue {
                statusCode(429)
            }

            val expect = deepLTranslatorClient.requestTranslate(ContentFixture.create(), Language.KOREAN)

            it("BadRequestException 예외를 던진다.") {
                val ex = shouldThrow<BadRequestException> { expect.block() }
                ex shouldHaveMessage "단기간에 너무 많은 요청을 보냈습니다."
            }
        }

        context("외부 서버가 456 응답을 반환하면") {
            mockWebServer.enqueue {
                statusCode(456)
            }

            val expect = deepLTranslatorClient.requestTranslate(ContentFixture.create(), Language.KOREAN)

            it("InternalServerError 예외를 던진다.") {
                val ex = shouldThrow<InternalServerError> { expect.block() }
                ex shouldHaveMessage "할당량이 초과되었습니다. 관리자에게 문의하세요."
            }
        }

        context("외부 서버가 그 외 응답을 반환하면") {
            mockWebServer.enqueue {
                statusCode(500)
            }

            val expect = deepLTranslatorClient.requestTranslate(ContentFixture.create(), Language.KOREAN)

            it("InternalServerError 예외를 던진다.") {
                val ex = shouldThrow<InternalServerError> { expect.block() }
                ex shouldHaveMessage "번역기 서버에 일시적 문제가 발생했습니다."
            }
        }

        context("외부 서버에 연결할 수 없으면") {
            mockWebServer.shutdown()

            val expect = deepLTranslatorClient.requestTranslate(ContentFixture.create(), Language.KOREAN)

            it("InternalServerError 예외를 던진다.") {
                val ex = shouldThrow<InternalServerError> { expect.block() }
                ex shouldHaveMessage "번역기 서버에 연결할 수 없습니다."
            }
        }
    }
})
