package kr.galaxyhub.sc.auth.infra

import kr.galaxyhub.sc.auth.domain.OAuth2Client
import kr.galaxyhub.sc.common.exception.BadRequestException
import kr.galaxyhub.sc.member.domain.SocialType
import kr.galaxyhub.sc.member.domain.UserInfo

class LocalOAuth2Client(
    private val memory: MutableMap<String, UserInfo>,
) : OAuth2Client {

    override fun getAccessToken(code: String): String {
        return code
    }

    override fun getUserInfo(accessToken: String): UserInfo {
        return memory[accessToken]
            ?: throw BadRequestException("잘못된 OAuth2 Authorization 코드입니다. available=${memory.keys}")
    }

    override fun getSocialType(): SocialType {
        return SocialType.LOCAL
    }

    fun clear() {
        memory.clear()
    }

    fun put(accessToken: String, userInfo: UserInfo) {
        memory[accessToken] = userInfo
    }
}
