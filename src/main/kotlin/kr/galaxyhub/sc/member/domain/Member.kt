package kr.galaxyhub.sc.member.domain

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import kr.galaxyhub.sc.common.domain.PrimaryKeyEntity

@Entity
class Member(
    socialId: String,
    socialType: SocialType,
    nickname: String,
    email: String?,
    profileImage: String?,
) : PrimaryKeyEntity() {

    @Column(name = "social_id", nullable = false)
    var socialId: String = socialId
        protected set

    @Enumerated(EnumType.STRING)
    @Column(name = "social_type", nullable = false, columnDefinition = "varchar")
    var socialType: SocialType = socialType
        protected set

    @Column(name = "nickname", nullable = false)
    var nickname: String = nickname
        protected set

    @Column(name = "email", nullable = true)
    var email: String? = email
        protected set

    @Column(name = "profile_image", nullable = true)
    var profileImage: String? = profileImage
        protected set
}
