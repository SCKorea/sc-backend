package kr.galaxyhub.sc.member.domain

import java.util.UUID

class MemoryMemberRepository(
    private val memory: MutableMap<UUID, Member> = mutableMapOf(),
) : MemberRepository {

    override fun findBySocialIdAndSocialType(socialId: String, socialType: SocialType): Member? {
        return memory.values.firstOrNull { it.socialId == socialId && it.socialType == socialType }
    }

    override fun save(member: Member): Member {
        memory[member.id] = member
        return member
    }

    fun clear() {
        memory.clear()
    }
}
