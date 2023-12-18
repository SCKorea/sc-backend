package kr.galaxyhub.sc.member.domain

data class UserInfo(
    val socialType: SocialType,
    val socialId: String,
    val email: String?,
    val nickname: String,
    val profileImage: String?,
)
