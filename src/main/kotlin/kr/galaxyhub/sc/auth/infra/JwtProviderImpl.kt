package kr.galaxyhub.sc.auth.infra

import kr.galaxyhub.sc.auth.domain.JwtProvider
import kr.galaxyhub.sc.member.domain.Member
import org.springframework.stereotype.Component

@Component
class JwtProviderImpl : JwtProvider {

    override fun provide(member: Member): String {
        // TODO 새로운 이슈로 만들어 기능 추가할 것
        return "accessToken"
    }
}
