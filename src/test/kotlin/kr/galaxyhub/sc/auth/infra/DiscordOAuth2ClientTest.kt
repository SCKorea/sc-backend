package kr.galaxyhub.sc.auth.infra

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.throwable.shouldHaveMessage
import java.time.Duration
import java.util.concurrent.TimeUnit
import kr.galaxyhub.sc.common.exception.BadRequestException
import kr.galaxyhub.sc.common.exception.InternalServerError
import kr.galaxyhub.sc.common.support.enqueue
import okhttp3.mockwebserver.MockWebServer
import org.springframework.web.reactive.function.client.WebClient

class DiscordOAuth2ClientTest : DescribeSpec({

    isolationMode = IsolationMode.InstancePerLeaf

    val mockWebServer = MockWebServer()
    val discordOAuth2Client = DiscordOAuth2Client(
        webClient = WebClient.builder()
            .baseUrl("${mockWebServer.url("/")}")
            .build(),
        clientId = "client_id",
        clientSecret = "client_secret",
        redirectUri = "https://sc.galaxyhub.kr",
        timeoutDuration = Duration.ofSeconds(10)
    )

    describe("getAccessToken") {
        val response = DiscordAccessTokenResponse(
            accessToken = "123123",
            tokenType = "Bearer",
            expiresIn = 3000,
            refreshToken = "321321",
            scope = "email"
        )

        context("외부 서버가 200 응답을 반환하면") {
            mockWebServer.enqueue {
                statusCode(200)
                body(response)
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

        context("외부 서버가 401 응답을 반환하면") {
            mockWebServer.enqueue { statusCode(401) }

            it("BadRequestException 예외를 던진다.") {
                val ex = shouldThrow<BadRequestException> { discordOAuth2Client.getAccessToken("wrong code") }
                ex shouldHaveMessage "잘못된 OAuth2 Authorization 코드입니다."
            }
        }

        context("외부 서버가 500 응답을 반환하면") {
            mockWebServer.enqueue { statusCode(500) }

            it("InternalServerError 예외를 던진다.") {
                val ex = shouldThrow<InternalServerError> { discordOAuth2Client.getAccessToken("code") }
                ex shouldHaveMessage "Discord OAuth2 서버에 문제가 발생했습니다."
            }
        }

        context("외부 서버에 연결할 수 없으면") {
            mockWebServer.shutdown()

            it("InternalServerError 예외를 던진다.") {
                val ex = shouldThrow<InternalServerError> { discordOAuth2Client.getAccessToken("code") }
                ex shouldHaveMessage "Discord OAuth2 서버와 연결 중 문제가 발생했습니다."
            }
        }

        context("외부 서버에 지연이 발생하면") {
            val delayClient = DiscordOAuth2Client(
                webClient = WebClient.builder()
                    .baseUrl("${mockWebServer.url("/")}")
                    .build(),
                clientId = "client_id",
                clientSecret = "client_secret",
                redirectUri = "https://sc.galaxyhub.kr",
                timeoutDuration = Duration.ofMillis(100)
            )
            mockWebServer.enqueue {
                statusCode(200)
                body(response)
                delay(10, TimeUnit.SECONDS)
            }

            it("InternalServerError 예외를 던진다.") {
                val ex = shouldThrow<InternalServerError> { delayClient.getAccessToken("code") }
                ex shouldHaveMessage "Discord OAuth2 서버의 응답 시간이 초과되었습니다."
            }
        }
    }

    describe("getUserInfo") {
        val response = DiscordUserInfoResponse(
            id = "12345",
            username = "seokjin8678",
            email = "seokjin8678@email.com",
            avatar = "avatar",
        )

        context("외부 서버가 200 응답을 반환하면") {
            mockWebServer.enqueue {
                statusCode(200)
                body(response)
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

        context("외부 서버에 연결할 수 없으면") {
            mockWebServer.shutdown()

            it("InternalServerError 예외를 던진다.") {
                val ex = shouldThrow<InternalServerError> { discordOAuth2Client.getUserInfo("accessToken") }
                ex shouldHaveMessage "Discord OAuth2 서버와 연결 중 문제가 발생했습니다."
            }
        }

        context("외부 서버에 지연이 발생하면") {
            val delayClient = DiscordOAuth2Client(
                webClient = WebClient.builder()
                    .baseUrl("${mockWebServer.url("/")}")
                    .build(),
                clientId = "client_id",
                clientSecret = "client_secret",
                redirectUri = "https://sc.galaxyhub.kr",
                timeoutDuration = Duration.ofMillis(100)
            )
            mockWebServer.enqueue {
                statusCode(200)
                body(response)
                delay(10, TimeUnit.SECONDS)
            }

            it("InternalServerError 예외를 던진다.") {
                val ex = shouldThrow<InternalServerError> { delayClient.getUserInfo("accessToken") }
                ex shouldHaveMessage "Discord OAuth2 서버의 응답 시간이 초과되었습니다."
            }
        }
    }
})

