package kr.galaxyhub.sc.member.domain

data class UserInfo(
    val socialType: SocialType,
    val socialId: String,
    val email: String?,
    val nickname: String,
    val profileImage: String?,
) {

    fun toMember(): Member {
        return Member(
            socialId = socialId,
            socialType = socialType,
            nickname = nickname,
            email = email,
            profileImage = profileImage
        )
    }
}
