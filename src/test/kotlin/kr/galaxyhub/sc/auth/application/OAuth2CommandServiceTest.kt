package kr.galaxyhub.sc.auth.application

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kr.galaxyhub.sc.auth.domain.JwtProvider
import kr.galaxyhub.sc.member.domain.MemoryMemberRepository
import kr.galaxyhub.sc.member.domain.SocialType
import kr.galaxyhub.sc.member.domain.UserInfo

class OAuth2CommandServiceTest(
    private val memoryMemberRepository: MemoryMemberRepository = MemoryMemberRepository(),
    private val fakeJwtProvider: JwtProvider = JwtProvider { "accessToken" },
    private val oAuth2CommandService: OAuth2CommandService = OAuth2CommandService(
        memoryMemberRepository,
        fakeJwtProvider
    ),
) : DescribeSpec({

    beforeContainer {
        memoryMemberRepository.clear()
    }

    describe("login") {
        val socialId = "1"
        val socialType = SocialType.LOCAL
        val userInfo = UserInfo(socialType, socialId, null, "seokjin8678", null)

        context("socialId와 socialType에 해당하는 회원이 없으면") {
            val expect = withContext(Dispatchers.IO) {
                oAuth2CommandService.login(userInfo)
            }

            it("정상적으로 UserResponse가 반환된다.") {
                expect.nickname shouldBe userInfo.nickname
            }

            it("새로운 회원이 가입된다.") {
                memoryMemberRepository.findBySocialIdAndSocialType(socialId, socialType)
            }
        }

        context("socialId와 socialType에 해당하는 회원이 있으면") {
            memoryMemberRepository.save(userInfo.toMember())

            val expect = withContext(Dispatchers.IO) {
                oAuth2CommandService.login(userInfo)
            }

            it("정상적으로 UserResponse가 반환된다.") {
                expect.nickname shouldBe userInfo.nickname
            }
        }
    }
})
