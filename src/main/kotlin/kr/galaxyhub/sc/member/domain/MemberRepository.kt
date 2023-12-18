package kr.galaxyhub.sc.member.domain

import java.util.UUID
import org.springframework.data.repository.Repository

interface MemberRepository : Repository<Member, UUID> {

    fun findBySocialIdAndSocialType(socialId: String, socialType: SocialType): Member?

    fun save(member: Member): Member
}
