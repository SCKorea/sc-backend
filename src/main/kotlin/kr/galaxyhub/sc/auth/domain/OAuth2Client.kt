package kr.galaxyhub.sc.auth.domain

import kr.galaxyhub.sc.member.domain.SocialType
import kr.galaxyhub.sc.member.domain.UserInfo

interface OAuth2Client {

    fun getAccessToken(code: String): String

    fun getUserInfo(accessToken: String): UserInfo

    fun getSocialType(): SocialType
}
