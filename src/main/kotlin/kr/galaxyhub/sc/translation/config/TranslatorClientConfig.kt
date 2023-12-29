package kr.galaxyhub.sc.translation.config

import java.time.Duration
import kr.galaxyhub.sc.translation.domain.TranslatorClient
import kr.galaxyhub.sc.translation.domain.TranslatorClients
import kr.galaxyhub.sc.translation.infra.DeepLTranslatorClient
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.http.HttpHeaders
import org.springframework.web.reactive.function.client.WebClient

@Configuration
class TranslatorClientConfig(
    @Value("\${galaxyhub.translator.deepl-api-key}") private val deepLApiKey: String,
    private val webClient: WebClient,
) {

    @Bean
    @Profile("!test")
    fun deepLTranslatorClient(): TranslatorClient {
        return DeepLTranslatorClient(
            webClient.mutate()
                .baseUrl("https://api.deepl.com")
                .defaultHeader(HttpHeaders.AUTHORIZATION, "DeepL-Auth-Key $deepLApiKey")
                .build(), Duration.ofSeconds(5)
        )
    }

    @Bean
    fun translatorClients(translatorClients: List<TranslatorClient>): TranslatorClients {
        return TranslatorClients.from(translatorClients)
    }
}
