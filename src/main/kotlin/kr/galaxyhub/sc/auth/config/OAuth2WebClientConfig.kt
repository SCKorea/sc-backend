package kr.galaxyhub.sc.auth.config

import java.time.Duration
import kr.galaxyhub.sc.auth.domain.OAuth2Client
import kr.galaxyhub.sc.auth.domain.OAuth2Clients
import kr.galaxyhub.sc.auth.infra.DiscordOAuth2Client
import kr.galaxyhub.sc.auth.infra.LocalOAuth2Client
import kr.galaxyhub.sc.member.domain.SocialType
import kr.galaxyhub.sc.member.domain.UserInfo
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.web.reactive.function.client.WebClient

@Configuration
class OAuth2WebClientConfig(
    private val webClient: WebClient,
) {

    @Bean
    fun discordOAuth2Client(
        @Value("\${galaxyhub.oauth2.discord.client_id}") clientId: String,
        @Value("\${galaxyhub.oauth2.discord.client_secret}") clientSecret: String,
        @Value("\${galaxyhub.oauth2.discord.redirect_uri}") redirectUri: String,
    ): OAuth2Client {
        return DiscordOAuth2Client(
            webClient = webClient.mutate()
                .baseUrl("https://discord.com")
                .build(),
            clientId = clientId,
            clientSecret = clientSecret,
            redirectUri = redirectUri,
            timeoutDuration = Duration.ofSeconds(5)
        )
    }

    @Bean
    @Profile("!prod")
    fun localOAuth2Client(): OAuth2Client {
        val memory = mutableMapOf<String, UserInfo>()
        memory["1"] = UserInfo(SocialType.LOCAL, "1", "seokjin8678@gmail.com", "seokjin8678", null)
        memory["2"] = UserInfo(SocialType.LOCAL, "2", "laeng@gmail.com", "laeng", null)
        return LocalOAuth2Client(memory)
    }

    @Bean
    fun oAuth2Clients(oAuth2Clients: List<OAuth2Client>): OAuth2Clients {
        return OAuth2Clients.from(oAuth2Clients)
    }
}
