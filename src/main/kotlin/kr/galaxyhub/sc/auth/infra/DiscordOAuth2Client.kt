package kr.galaxyhub.sc.auth.infra

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import io.github.oshai.kotlinlogging.KotlinLogging
import kr.galaxyhub.sc.auth.domain.OAuth2Client
import kr.galaxyhub.sc.common.exception.BadRequestException
import kr.galaxyhub.sc.common.exception.InternalServerError
import kr.galaxyhub.sc.member.domain.SocialType
import kr.galaxyhub.sc.member.domain.UserInfo
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.reactive.function.client.ClientResponse
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono

private val log = KotlinLogging.logger {}

class DiscordOAuth2Client(
    private val webClient: WebClient,
    private val clientId: String,
    private val clientSecret: String,
    private val redirectUri: String,
) : OAuth2Client {

    override fun getAccessToken(code: String): String {
        return webClient.post()
            .uri("/api/oauth2/token")
            .bodyValue(createAccessTokenBodyForm(code))
            .retrieve()
            .onStatus({ it.is4xxClientError || it.is5xxServerError }) {
                throw handleAccessTokenError(it)
            }
            .bodyToMono<DiscordAccessTokenResponse>()
            .block()!!
            .accessToken
    }

    private fun handleAccessTokenError(clientResponse: ClientResponse): Exception {
        return when (clientResponse.statusCode()) {
            HttpStatus.UNAUTHORIZED -> {
                BadRequestException("잘못된 OAuth2 Authorization 코드입니다.")
            }

            else -> {
                log.warn { "getAccessToken() 호출에서 ${clientResponse.statusCode()} 예외가 발생했습니다." }
                InternalServerError("Discord OAuth2 서버에 문제가 발생했습니다.")
            }
        }
    }

    private fun createAccessTokenBodyForm(code: String): MultiValueMap<String, String> {
        val multiValueMap = LinkedMultiValueMap<String, String>()
        multiValueMap.add("client_id", clientId)
        multiValueMap.add("client_secret", clientSecret)
        multiValueMap.add("code", code)
        multiValueMap.add("grant_type", "authorization_code")
        multiValueMap.add("redirect_uri", redirectUri)
        return multiValueMap
    }

    override fun getUserInfo(accessToken: String): UserInfo {
        return webClient.get()
            .uri("/api/users/@me")
            .header(HttpHeaders.AUTHORIZATION, "Bearer $accessToken")
            .retrieve()
            .onStatus({ it.is4xxClientError || it.is5xxServerError }) {
                log.warn { "getUserInfo() 호출에서 ${it.statusCode()} 예외가 발생했습니다." }
                throw InternalServerError("Discord OAuth2 서버에 문제가 발생했습니다.")
            }
            .bodyToMono<DiscordUserInfoResponse>()
            .block()!!
            .toUserInfo()
    }

    override fun getSocialType(): SocialType {
        return SocialType.DISCORD
    }
}

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class DiscordAccessTokenResponse(
    val accessToken: String,
    val tokenType: String,
    val expiresIn: Long,
    val refreshToken: String,
    val scope: String,
)

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class DiscordUserInfoResponse(
    val id: String,
    val username: String,
    val email: String?,
    val avatar: String?,
) {

    fun toUserInfo(): UserInfo {
        return UserInfo(
            email = email,
            nickname = username,
            socialType = SocialType.DISCORD,
            socialId = id,
            profileImage = avatar?.let { "https://cdn.discordapp.com/avatars/${id}/${avatar}.png" }
        )
    }
}
