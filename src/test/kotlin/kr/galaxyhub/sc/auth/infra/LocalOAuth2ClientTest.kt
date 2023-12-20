package kr.galaxyhub.sc.auth.infra

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.throwable.shouldHaveMessage
import kr.galaxyhub.sc.common.exception.BadRequestException
import kr.galaxyhub.sc.member.domain.SocialType
import kr.galaxyhub.sc.member.domain.UserInfo

class LocalOAuth2ClientTest(
    private val localOAuth2Client: LocalOAuth2Client = LocalOAuth2Client(mutableMapOf()),
) : DescribeSpec({

    beforeContainer {
        localOAuth2Client.clear()
    }

    describe("getAccessToken") {
        context("AccessToken을 요청하면") {
            val expect = localOAuth2Client.getAccessToken("code")

            it("파라미터로 넣은 code가 그대로 반환된다.") {
                expect shouldBe "code"
            }
        }
    }

    describe("getUserInfo") {
        val accessToken = "1"

        context("accessToken에 해당하는 사용자가 있으면") {
            val actual = UserInfo(SocialType.LOCAL, "1", null, "seokjin8678", null)
            localOAuth2Client.put(accessToken, actual)

            val expect = localOAuth2Client.getUserInfo(accessToken)

            it("UserInfo가 반환된다.") {
                expect shouldBe actual
            }
        }

        context("accessToken에 해당하는 사용자가 없으면") {
            it("BadRequestException 예외를 던진다.") {
                val ex = shouldThrow<BadRequestException> { localOAuth2Client.getUserInfo(accessToken) }
                ex shouldHaveMessage "잘못된 OAuth2 Authorization 코드입니다. available=[]"
            }
        }
    }
})
