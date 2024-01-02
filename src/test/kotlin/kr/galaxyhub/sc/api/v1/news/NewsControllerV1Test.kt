package kr.galaxyhub.sc.api.v1.news

import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import io.kotest.core.spec.style.DescribeSpec
import io.mockk.every
import io.mockk.justRun
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.UUID
import kr.galaxyhub.sc.api.support.ARRAY
import kr.galaxyhub.sc.api.support.ENUM
import kr.galaxyhub.sc.api.support.NUMBER
import kr.galaxyhub.sc.api.support.OBJECT
import kr.galaxyhub.sc.api.support.STRING
import kr.galaxyhub.sc.api.support.ZONEDDATETIME
import kr.galaxyhub.sc.api.support.andDocument
import kr.galaxyhub.sc.api.support.docGet
import kr.galaxyhub.sc.api.support.docPost
import kr.galaxyhub.sc.api.support.param
import kr.galaxyhub.sc.api.support.pathMeans
import kr.galaxyhub.sc.api.support.type
import kr.galaxyhub.sc.api.v1.news.dto.NewsCreateRequest
import kr.galaxyhub.sc.api.v1.news.dto.NewsUpdateRequest
import kr.galaxyhub.sc.news.application.NewsCommandService
import kr.galaxyhub.sc.news.application.NewsQueryService
import kr.galaxyhub.sc.news.application.dto.NewsDetailResponse
import kr.galaxyhub.sc.news.application.dto.NewsResponse
import kr.galaxyhub.sc.news.domain.Language
import kr.galaxyhub.sc.news.domain.NewsType
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc

@WebMvcTest(NewsControllerV1::class)
@AutoConfigureRestDocs
class NewsControllerV1Test(
    private val mockMvc: MockMvc,
    private val objectMapper: ObjectMapper,
    @MockkBean
    private val newsQueryService: NewsQueryService,
    @MockkBean
    private val newsCommandService: NewsCommandService,
) : DescribeSpec({

    describe("GET /api/v1/news/{newsId}") {
        context("유효한 요청이 전달되면") {
            val newsId = UUID.randomUUID()
            val response = newsDetailResponse(newsId)
            every { newsQueryService.getDetailByIdAndLanguage(newsId, Language.ENGLISH) } returns response

            it("200 응답과 뉴스의 상세 정보가 조회된다.") {
                mockMvc.docGet("/api/v1/news/{newsId}", newsId) {
                    contentType = MediaType.APPLICATION_JSON
                    param("language" to Language.ENGLISH)
                }.andExpect {
                    status { isOk() }
                }.andDocument("news/find-detail") {
                    pathParameters(
                        "newsId" pathMeans "뉴스 식별자" constraint "UUID"
                    )
                    queryParameters(
                        "language" pathMeans "뉴스 언어" formattedAs ENUM(Language::class)
                    )
                    responseBody(
                        "data" type OBJECT means "뉴스 상세 정보",
                        "data.id" type STRING means "뉴스 식별자",
                        "data.newsType" type ENUM(NewsType::class) means "뉴스 타입",
                        "data.title" type STRING means "뉴스 제목",
                        "data.excerpt" type STRING means "뉴스 발췌",
                        "data.language" type ENUM(Language::class) means "뉴스 언어",
                        "data.publishedAt" type ZONEDDATETIME means "뉴스 발행 시간",
                        "data.content" type STRING means "뉴스 내용",
                        "data.supportLanguages" type ARRAY means "뉴스 지원 언어",
                        "data.originId" type NUMBER means "원본 뉴스 식별자",
                        "data.originUrl" type STRING means "원본 뉴스의 URL",
                    )
                }
            }
        }
    }

    describe("GET /api/v1/news") {
        context("유효한 요청이 전달되면") {
            val response = listOf(newsResponse())
            every { newsQueryService.findAll() } returns response

            it("200 응답과 모든 뉴스의 정보가 조회된다.") {
                mockMvc.docGet("/api/v1/news") {
                    contentType = MediaType.APPLICATION_JSON
                }.andExpect {
                    status { isOk() }
                }.andDocument("news/find-all") {
                    responseBody(
                        "data" type ARRAY means "뉴스 목록",
                        "data[0].id" type STRING means "뉴스 식별자",
                        "data[0].newsType" type ENUM(NewsType::class) means "뉴스 타입",
                        "data[0].title" type STRING means "뉴스 제목",
                        "data[0].excerpt" type STRING means "뉴스 발췌",
                        "data[0].publishedAt" type ZONEDDATETIME means "뉴스 발행 시간",
                        "data[0].supportLanguages" type ARRAY means "뉴스 지원 언어",
                        "data[0].originId" type NUMBER means "원본 뉴스 식별자",
                        "data[0].originUrl" type STRING means "원본 뉴스의 URL",
                    )
                }
            }
        }
    }

    describe("POST /api/v1/news") {
        context("유효한 요청이 전달되면") {
            val request = newsCreateRequest()
            every { newsCommandService.create(any()) } returns UUID.randomUUID()

            it("201 응답과 뉴스의 식별자가 반환된다.") {
                mockMvc.docPost("/api/v1/news") {
                    contentType = MediaType.APPLICATION_JSON
                    content = objectMapper.writeValueAsString(request)
                }.andExpect {
                    status { isCreated() }
                }.andDocument("news/create") {
                    requestBody(
                        "newsType" type ENUM(NewsType::class) means "뉴스 타입",
                        "title" type STRING means "뉴스 제목",
                        "excerpt" type STRING means "뉴스 발췌" isOptional true,
                        "publishedAt" type ZONEDDATETIME means "뉴스 발행 시간",
                        "originId" type NUMBER means "원본 뉴스 식별자",
                        "originUrl" type STRING means "원본 뉴스의 URL",
                        "language" type ENUM(Language::class) means "뉴스 언어",
                        "content" type STRING means "뉴스 내용"
                    )
                    responseBody(
                        "data" type STRING means "생성한 뉴스의 식별자"
                    )
                }
            }
        }
    }

    describe("POST /api/v1/news/{newsId}/content") {
        context("유효한 요청이 전달되면") {
            val newsId = UUID.randomUUID()
            val request = newsUpdateRequest()
            justRun { newsCommandService.updateContent(any(), any()) }

            it("200 응답이 반환된다.") {
                mockMvc.docPost("/api/v1/news/{newsId}/content", newsId) {
                    contentType = MediaType.APPLICATION_JSON
                    content = objectMapper.writeValueAsString(request)
                }.andExpect {
                    status { isOk() }
                }.andDocument("news/update-content") {
                    pathParameters(
                        "newsId" pathMeans "뉴스 식별자" constraint "UUID"
                    )
                    requestBody(
                        "language" type ENUM(Language::class) means "뉴스 언어",
                        "title" type STRING means "뉴스 제목" isOptional true,
                        "excerpt" type STRING means "뉴스 발췌" isOptional true,
                        "content" type STRING means "뉴스 내용" isOptional true
                    )
                }
            }
        }
    }
})

private fun newsUpdateRequest() = NewsUpdateRequest(
    language = Language.ENGLISH,
    title = "Star Citizen Live",
    excerpt = "You asked. We're answering! Join us today for a live Q&A show with the Vehicle Gameplay team.",
    content = "blah blah"
)

private fun newsCreateRequest() = NewsCreateRequest(
    newsType = NewsType.NEWS,
    title = "Star Citizen Live",
    excerpt = "You asked. We're answering! Join us today for a live Q&A show with the Vehicle Gameplay team.",
    publishedAt = ZonedDateTime.of(LocalDateTime.parse("2023-12-04T03:53:33"), ZoneId.systemDefault()),
    originId = 1,
    originUrl = "https://sc.galaxyhub.kr/",
    language = Language.KOREAN,
    content = "어쩌구 저쩌구"
)

private fun newsDetailResponse(id: UUID) = NewsDetailResponse(
    id = id,
    newsType = NewsType.NEWS,
    title = "Star Citizen Live",
    excerpt = "You asked. We're answering! Join us today for a live Q&A show with the Vehicle Gameplay team.",
    language = Language.ENGLISH,
    publishedAt = ZonedDateTime.of(LocalDateTime.parse("2023-12-09T11:49:44"), ZoneId.systemDefault()),
    content = "blah blah",
    originId = 1,
    originUrl = "https://sc.galaxyhub.kr/",
    supportLanguages = setOf(Language.ENGLISH)
)

private fun newsResponse() = NewsResponse(
    id = UUID.randomUUID(),
    newsType = NewsType.NEWS,
    title = "Star Citizen Live",
    excerpt = "You asked. We're answering! Join us today for a live Q&A show with the Vehicle Gameplay team.",
    publishedAt = ZonedDateTime.of(LocalDateTime.parse("2023-12-09T11:49:44"), ZoneId.systemDefault()),
    originId = 1,
    originUrl = "https://sc.galaxyhub.kr/",
    supportLanguages = setOf(Language.ENGLISH)
)
