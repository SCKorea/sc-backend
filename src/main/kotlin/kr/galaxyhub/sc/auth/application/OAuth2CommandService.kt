package kr.galaxyhub.sc.auth.application

import kr.galaxyhub.sc.auth.application.dto.LoginResponse
import kr.galaxyhub.sc.auth.domain.JwtProvider
import kr.galaxyhub.sc.member.domain.Member
import kr.galaxyhub.sc.member.domain.MemberRepository
import kr.galaxyhub.sc.member.domain.UserInfo
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class OAuth2CommandService(
    private val memberRepository: MemberRepository,
    private val jwtProvider: JwtProvider,
) {

    fun login(userInfo: UserInfo): LoginResponse {
        val member = memberRepository.findBySocialIdAndSocialType(userInfo.socialId, userInfo.socialType)
            ?: signup(userInfo)
        val accessToken = jwtProvider.provide(member)
        return LoginResponse(accessToken, member.nickname, member.profileImage)
    }

    private fun signup(userInfo: UserInfo): Member {
        val member = userInfo.toMember()
        return memberRepository.save(member)
    }
}
