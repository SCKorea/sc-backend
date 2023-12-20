package kr.galaxyhub.sc.auth.config

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import java.util.Date
import java.util.concurrent.TimeUnit
import kr.galaxyhub.sc.auth.domain.JwtExtractor
import kr.galaxyhub.sc.auth.domain.JwtProvider
import kr.galaxyhub.sc.auth.infra.JwtExtractorImpl
import kr.galaxyhub.sc.auth.infra.JwtProviderImpl
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class JwtConfig(
    @Value("\${galaxyhub.jwt.secret-key}") private val secretKey: String,
) {

    @Bean
    fun jwtProvider(): JwtProvider {
        val expirationMilliseconds: Long = TimeUnit.MINUTES.toMillis(30)
        return JwtProviderImpl(
            Keys.hmacShaKeyFor(secretKey.encodeToByteArray()),
            ::Date, // == () -> new Date()
            expirationMilliseconds,
        )
    }

    @Bean
    fun jwtExtractor(): JwtExtractor {
        val jwtParser = Jwts.parser()
            .clock(::Date)
            .verifyWith(Keys.hmacShaKeyFor(secretKey.encodeToByteArray()))
            .build()
        return JwtExtractorImpl(jwtParser)
    }
}
