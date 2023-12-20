package kr.galaxyhub.sc.auth.domain

import kr.galaxyhub.sc.member.domain.Member

fun interface JwtProvider {

    fun provide(member: Member): String
}
