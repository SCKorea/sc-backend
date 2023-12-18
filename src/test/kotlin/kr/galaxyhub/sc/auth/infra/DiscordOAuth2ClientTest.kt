package kr.galaxyhub.sc.auth.infra

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.throwable.shouldHaveMessage
import kr.galaxyhub.sc.common.exception.InternalServerError
import kr.galaxyhub.sc.common.support.enqueue
import okhttp3.mockwebserver.MockWebServer
import org.springframework.web.reactive.function.client.WebClient

class DiscordOAuth2ClientTest : DescribeSpec({

    val objectMapper = jacksonObjectMapper()
    val mockWebServer = MockWebServer()
    val discordOAuth2Client = DiscordOAuth2Client(
        webClient = WebClient.builder()
            .baseUrl("${mockWebServer.url("/")}")
            .build(),
        clientId = "client_id",
        clientSecret = "client_secret",
        redirectUri = "https://sc.galaxyhub.kr"
    )

    describe("getAccessToken") {
        context("외부 서버가 200 응답을 반환하면") {
            val response = DiscordAccessTokenResponse(
                accessToken = "123123",
                tokenType = "Bearer",
                expiresIn = 3000,
                refreshToken = "321321",
                scope = "email"
            )
            mockWebServer.enqueue {
                statusCode(200)
                body(objectMapper.writeValueAsString(response))
            }

            val actual = response.accessToken
            val expect = discordOAuth2Client.getAccessToken("code")

            it("AccessToken이 정상적으로 반환된다.") {
                expect shouldBe actual
            }
        }

        context("외부 서버가 400 응답을 반환하면") {
            mockWebServer.enqueue { statusCode(400) }

            it("InternalServerError 예외를 던진다.") {
                val ex = shouldThrow<InternalServerError> { discordOAuth2Client.getAccessToken("code") }
                ex shouldHaveMessage "Discord OAuth2 서버에 문제가 발생했습니다."
            }
        }

        context("외부 서버가 500 응답을 반환하면") {
            mockWebServer.enqueue { statusCode(500) }

            it("InternalServerError 예외를 던진다.") {
                val ex = shouldThrow<InternalServerError> { discordOAuth2Client.getAccessToken("code") }
                ex shouldHaveMessage "Discord OAuth2 서버에 문제가 발생했습니다."
            }
        }
    }

    describe("getUserInfo") {
        context("외부 서버가 200 응답을 반환하면") {
            val response = DiscordUserInfoResponse(
                id = "12345",
                username = "seokjin8678",
                email = "seokjin8678@email.com",
                avatar = "avatar",
            )
            mockWebServer.enqueue {
                statusCode(200)
                body(objectMapper.writeValueAsString(response))
            }

            val actual = response.toUserInfo()
            val expect = discordOAuth2Client.getUserInfo("accessToken")

            it("UserInfo가 정상적으로 반환된다.") {
                expect shouldBe actual
            }

            it("profileImage는 https://cdn.discordapp.com/avatars/{id}/{avatar}.png 형식이다.") {
                expect.profileImage shouldBe "https://cdn.discordapp.com/avatars/${response.id}/${response.avatar}.png"
            }
        }

        context("외부 서버가 400 응답을 반환하면") {
            mockWebServer.enqueue { statusCode(400) }

            it("InternalServerError 예외를 던진다.") {
                val ex = shouldThrow<InternalServerError> { discordOAuth2Client.getUserInfo("accessToken") }
                ex shouldHaveMessage "Discord OAuth2 서버에 문제가 발생했습니다."
            }
        }

        context("외부 서버가 500 응답을 반환하면") {
            mockWebServer.enqueue { statusCode(500) }

            it("InternalServerError 예외를 던진다.") {
                val ex = shouldThrow<InternalServerError> { discordOAuth2Client.getUserInfo("accessToken") }
                ex shouldHaveMessage "Discord OAuth2 서버에 문제가 발생했습니다."
            }
        }
    }
})

