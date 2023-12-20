package kr.galaxyhub.sc.api.v1.auth.oauth2

import com.ninjasquad.springmockk.MockkBean
import io.kotest.core.spec.style.DescribeSpec
import io.mockk.every
import kr.galaxyhub.sc.api.support.ENUM
import kr.galaxyhub.sc.api.support.STRING
import kr.galaxyhub.sc.api.support.andDocument
import kr.galaxyhub.sc.api.support.docGet
import kr.galaxyhub.sc.api.support.param
import kr.galaxyhub.sc.api.support.pathMeans
import kr.galaxyhub.sc.api.support.type
import kr.galaxyhub.sc.auth.application.OAuth2FacadeService
import kr.galaxyhub.sc.auth.application.dto.LoginResponse
import kr.galaxyhub.sc.member.domain.SocialType
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc

@WebMvcTest(OAuth2ControllerV1::class)
@AutoConfigureRestDocs
class OAuth2ControllerV1Test(
    private val mockMvc: MockMvc,
    @MockkBean
    private val oAuth2FacadeService: OAuth2FacadeService,
) : DescribeSpec({

    describe("POST /api/v1/auth/oauth2/login") {
        context("유효한 요청이 전달되면") {
            it("200 응답과 로그인 정보가 반환된다.") {
                val response = LoginResponse("abc123abc123", "seokjin8678", "https://sc.galaxyhub.kr/image.png")
                every { oAuth2FacadeService.login(any(), any()) } returns response

                mockMvc.docGet("/api/v1/auth/oauth2/login") {
                    contentType = MediaType.APPLICATION_JSON
                    param("code" to "1123123")
                    param("socialType" to "DISCORD")
                }.andExpect {
                    status { isOk() }
                }.andDocument("auth/oauth2/login") {
                    queryParameters(
                        "code" pathMeans "Authorization 코드",
                        "socialType" pathMeans "OAuth2 소셜 타입" formattedAs ENUM(SocialType.entries.filter { it != SocialType.LOCAL }),
                    )
                    responseBody(
                        "data.accessToken" type STRING means "Bearer JWT Access Token",
                        "data.nickname" type STRING means "사용자의 닉네임",
                        "data.imageUrl" type STRING means "사용자의 프로필 사진 링크" isOptional true,
                    )
                }
            }
        }
    }
})
