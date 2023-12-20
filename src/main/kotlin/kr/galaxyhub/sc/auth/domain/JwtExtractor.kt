package kr.galaxyhub.sc.auth.domain

import java.util.UUID

fun interface JwtExtractor {

    fun parse(jwtToken: String): UUID
}
