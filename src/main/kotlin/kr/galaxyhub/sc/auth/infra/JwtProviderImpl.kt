package kr.galaxyhub.sc.auth.infra

import io.jsonwebtoken.Clock
import io.jsonwebtoken.Jwts
import java.util.Date
import javax.crypto.SecretKey
import kr.galaxyhub.sc.auth.domain.JwtProvider
import kr.galaxyhub.sc.member.domain.Member

class JwtProviderImpl(
    private val secretKey: SecretKey,
    private val clock: Clock,
    private val expirationMilliseconds: Long,
) : JwtProvider {

    override fun provide(member: Member): String {
        val now = clock.now()
        return Jwts.builder()
            .signWith(secretKey)
            .claim("memberId", member.id)
            .issuedAt(now)
            .expiration(Date(now.time + expirationMilliseconds))
            .compact()
    }
}
