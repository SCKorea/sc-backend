package kr.galaxyhub.sc.api.v1.crawler

import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import io.kotest.core.spec.style.DescribeSpec
import io.mockk.every
import java.util.UUID
import kr.galaxyhub.sc.api.support.STRING
import kr.galaxyhub.sc.api.support.andDocument
import kr.galaxyhub.sc.api.support.docPost
import kr.galaxyhub.sc.api.support.type
import kr.galaxyhub.sc.api.v1.crawler.dto.NewsCrawlingRequest
import kr.galaxyhub.sc.crawler.application.CrawlerCommandService
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc

@WebMvcTest(CrawlerControllerV1::class)
@AutoConfigureRestDocs
class CrawlerControllerV1Test(
    private val mockMvc: MockMvc,
    private val objectMapper: ObjectMapper,
    @MockkBean
    private val crawlerCommandService: CrawlerCommandService,
) : DescribeSpec({

    describe("POST /api/v1/crawler") {
        context("유요한 요청이 전달되면") {
            val request =
                NewsCrawlingRequest("https://robertsspaceindustries.com/comm-link/engineering/12345-blah-blah")
            every { crawlerCommandService.crawling(any()) } returns UUID.randomUUID()

            it("201 응답과 뉴스의 식별자가 반환된다.") {
                mockMvc.docPost("/api/v1/crawler") {
                    contentType = MediaType.APPLICATION_JSON
                    content = objectMapper.writeValueAsString(request)
                }.andExpect {
                    status { isCreated() }
                }.andDocument("crawler/crawling") {
                    requestBody(
                        "url" type STRING means "크롤링할 뉴스의 URL",
                    )
                    responseBody(
                        "data" type STRING means "크롤링되서 생성된 뉴스의 식별자"
                    )
                }
            }
        }
    }
})
