package kr.galaxyhub.sc.auth.infra

import io.github.oshai.kotlinlogging.KotlinLogging
import io.jsonwebtoken.Claims
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.JwtException
import io.jsonwebtoken.JwtParser
import java.util.UUID
import kr.galaxyhub.sc.auth.domain.JwtExtractor
import kr.galaxyhub.sc.common.exception.InternalServerError
import kr.galaxyhub.sc.common.exception.UnauthorizedException

private val log = KotlinLogging.logger {}

class JwtExtractorImpl(
    private val jwtParser: JwtParser,
) : JwtExtractor {

    override fun parse(jwtToken: String): UUID {
        if (!jwtParser.isSigned(jwtToken)) { // Javadoc에 파싱 전 isSigned 메서드를 호출하는 것이 더 효율적이라고 하여 적용
            throw UnauthorizedException("잘못된 형식의 JWT 토큰 입니다.")
        }
        val claims = extractClaims(jwtToken)
        val memberId = getMemberIdFrom(claims)
        return UUID.fromString(memberId)
    }

    private fun getMemberIdFrom(claims: Claims): String {
        return (claims["memberId"] as? String)
            ?: run {
                log.error { "JWT 토큰 페이로드에 memberId가 없습니다. claims=$claims" }
                throw InternalServerError("서버에서 JWT 토큰 파싱 중 문제가 발생했습니다.")
            }
    }

    private fun extractClaims(jwtToken: String): Claims {
        return runCatching { jwtParser.parseSignedClaims(jwtToken).payload }
            .onFailure { handleJwtException(it) }
            .getOrThrow()
    }

    private fun handleJwtException(it: Throwable) {
        throw when (it) {
            is ExpiredJwtException -> UnauthorizedException("만료된 JWT 토큰 입니다.")
            is JwtException, is IllegalArgumentException -> UnauthorizedException("잘못된 형식의 JWT 토큰 입니다.")
            else -> {
                log.error(it) { "서버에서 JWT 토큰 파싱 중 문제가 발생했습니다." }
                it
            }
        }
    }
}

