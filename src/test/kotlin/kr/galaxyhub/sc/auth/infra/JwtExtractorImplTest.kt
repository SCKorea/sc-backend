package kr.galaxyhub.sc.auth.infra

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.throwable.shouldHaveMessage
import java.time.Instant
import java.util.Date
import java.util.UUID
import kr.galaxyhub.sc.common.exception.InternalServerError
import kr.galaxyhub.sc.common.exception.UnauthorizedException

class JwtExtractorImplTest : DescribeSpec({

    val secretKey = Keys.hmacShaKeyFor("galaxyhub".repeat(10).encodeToByteArray())
    val otherKey = Keys.hmacShaKeyFor("seokjin8678".repeat(10).encodeToByteArray())
    val now = Date.from(Instant.parse("2023-12-20T17:09:00Z"))

    describe("parse") {
        val jwtExtractor = JwtExtractorImpl(
            Jwts.parser()
                .verifyWith(secretKey)
                .clock { now }
                .build()
        )

        context("유효하지 않은 JWT 토큰이면") {
            val token = ""

            it("UnauthorizedException 예외를 던진다.") {
                val ex = shouldThrow<UnauthorizedException> {
                    jwtExtractor.parse(token)
                }
                ex shouldHaveMessage "잘못된 형식의 JWT 토큰 입니다."
            }
        }

        context("JWT 토큰이 만료되면") {
            val token = Jwts.builder()
                .signWith(secretKey)
                .expiration(Date(now.time - 1_000))
                .compact()

            it("UnauthorizedException 예외를 던진다.") {
                val ex = shouldThrow<UnauthorizedException> {
                    jwtExtractor.parse(token)
                }
                ex shouldHaveMessage "만료된 JWT 토큰 입니다."
            }
        }

        context("JWT 토큰이 서명 되지 않으면") {
            val token = Jwts.builder()
                .compact()

            it("UnauthorizedException 예외를 던진다.") {
                val ex = shouldThrow<UnauthorizedException> {
                    jwtExtractor.parse(token)
                }
                ex shouldHaveMessage "잘못된 형식의 JWT 토큰 입니다."
            }
        }

        context("JWT 토큰이 다른 secretKey로 서명 되면") {
            val token = Jwts.builder()
                .signWith(otherKey)
                .compact()

            it("UnauthorizedException 예외를 던진다.") {
                val ex = shouldThrow<UnauthorizedException> {
                    jwtExtractor.parse(token)
                }
                ex shouldHaveMessage "잘못된 형식의 JWT 토큰 입니다."
            }
        }

        context("JWT 토큰에 memberId 페이로드가 없으면") {
            val token = Jwts.builder()
                .signWith(secretKey)
                .expiration((Date(now.time + 1_000)))
                .compact()

            it("InternalServerError 예외를 던진다.") {
                val ex = shouldThrow<InternalServerError> {
                    jwtExtractor.parse(token)
                }
                ex shouldHaveMessage "서버에서 JWT 토큰 파싱 중 문제가 발생했습니다."
            }
        }

        context("JWT 토큰이 유효하면") {
            val memberId = UUID.randomUUID()
            val token = Jwts.builder()
                .claim("memberId", memberId)
                .signWith(secretKey)
                .expiration((Date(now.time + 1_000)))
                .compact()

            it("memberId가 반환된다.") {
                jwtExtractor.parse(token) shouldBe memberId
            }
        }
    }
})
