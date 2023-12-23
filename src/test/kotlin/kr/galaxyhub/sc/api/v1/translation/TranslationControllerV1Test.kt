package kr.galaxyhub.sc.api.v1.translation

import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import io.kotest.core.spec.style.DescribeSpec
import io.mockk.every
import java.util.UUID
import kr.galaxyhub.sc.api.support.ENUM
import kr.galaxyhub.sc.api.support.STRING
import kr.galaxyhub.sc.api.support.andDocument
import kr.galaxyhub.sc.api.support.docGet
import kr.galaxyhub.sc.api.support.docPost
import kr.galaxyhub.sc.api.support.pathMeans
import kr.galaxyhub.sc.api.support.type
import kr.galaxyhub.sc.api.v1.translation.dto.TranslationRequest
import kr.galaxyhub.sc.news.domain.Language
import kr.galaxyhub.sc.translation.application.TranslationCommandService
import kr.galaxyhub.sc.translation.application.TranslationQueryService
import kr.galaxyhub.sc.translation.application.dto.TranslationResponse
import kr.galaxyhub.sc.translation.domain.TranslationStatus
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc

@WebMvcTest(TranslationControllerV1::class)
@AutoConfigureRestDocs
class TranslationControllerV1Test(
    private val mockMvc: MockMvc,
    private val objectMapper: ObjectMapper,
    @MockkBean
    private val translationCommandService: TranslationCommandService,
    @MockkBean
    private val translationQueryService: TranslationQueryService,
) : DescribeSpec({

    describe("POST /api/v1/translation/{newsId}") {
        context("유효한 요청이 전달되면") {
            val request = TranslationRequest(Language.KOREAN)
            val newsId = UUID.randomUUID()
            every { translationCommandService.translate(any()) } returns UUID.randomUUID()

            it("201 응답과 번역 진행 상황의 식별자가 반환된다.") {
                mockMvc.docPost("/api/v1/translation/{newsId}", newsId) {
                    contentType = MediaType.APPLICATION_JSON
                    content = objectMapper.writeValueAsString(request)
                }.andExpect {
                    status { isCreated() }
                }.andDocument("translation/translate") {
                    pathParameters(
                        "newsId" pathMeans "번역할 뉴스의 식별자"
                    )
                    requestBody(
                        "destinationLanguage" type ENUM(Language::class) means "번역 도착 언어"
                    )
                    responseBody(
                        "data" type STRING means "번역 진행 상황의 식별자"
                    )
                }
            }
        }
    }

    describe("GET /api/v1/translation/{translateProgressionId}") {
        context("유효한 요청이 전달되면") {
            val translateProgressionId = UUID.randomUUID()
            val response = translationResponse(translateProgressionId)
            every { translationQueryService.findById(any()) } returns response

            it("200 응답과 번역 진행 상황의 정보가 조회된다.") {
                mockMvc.docGet("/api/v1/translation/{translateProgressionId}", translateProgressionId) {
                    contentType = MediaType.APPLICATION_JSON
                }.andExpect {
                    status { isOk() }
                }.andDocument("translation/find-by-id") {
                    pathParameters(
                        "translateProgressionId" pathMeans "번역 진행 상황의 식별자"
                    )
                    responseBody(
                        "data.translateProgressionId" type STRING means "번역 진행 상황의 식별자",
                        "data.targetNewsId" type STRING means "번역할 뉴스의 식별자",
                        "data.translationStatus" type ENUM(TranslationStatus::class) means "번역 상태",
                        "data.message" type STRING means "번역 진행 상황의 추가적 메시지" isOptional true
                    )
                }
            }
        }
    }
})

private fun translationResponse(translateProgressionId: UUID) = TranslationResponse(
    translateProgressionId = translateProgressionId,
    targetNewsId = UUID.randomUUID(),
    translationStatus = TranslationStatus.PROGRESS,
)
