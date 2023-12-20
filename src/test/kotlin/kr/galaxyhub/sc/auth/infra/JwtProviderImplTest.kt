package kr.galaxyhub.sc.auth.infra

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import io.kotest.assertions.throwables.shouldNotThrow
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import java.time.Instant
import java.util.Date
import java.util.concurrent.TimeUnit
import kr.galaxyhub.sc.member.domain.Member
import kr.galaxyhub.sc.member.domain.SocialType

class JwtProviderImplTest : DescribeSpec({

    val secretKey = Keys.hmacShaKeyFor("galaxyhub".repeat(10).encodeToByteArray())
    val otherKey = Keys.hmacShaKeyFor("seokjin8678".repeat(10).encodeToByteArray())
    val now = Date.from(Instant.parse("2023-12-20T17:09:00Z"))

    describe("provide") {
        val jwtProvider = JwtProviderImpl(secretKey, { now }, TimeUnit.MINUTES.toMillis(30))

        context("secretKey로 JWT 토큰을 생성하면") {
            val member = Member("1", SocialType.LOCAL, "seokjin8678", null, null)

            val token = jwtProvider.provide(member)

            it("토큰은 secretKey로 검증할 수 있다.") {
                val jwtParser = Jwts.parser()
                    .verifyWith(secretKey)
                    .clock { now }
                    .build()

                shouldNotThrow<Exception> { jwtParser.parseSignedClaims(token) }
            }

            it("토큰은 다른 secretKey로 검증할 수 없다.") {
                val otherParser = Jwts.parser()
                    .verifyWith(otherKey)
                    .clock { now }
                    .build()

                shouldThrow<Exception> { otherParser.parseSignedClaims(token) }
            }
        }
    }
})
