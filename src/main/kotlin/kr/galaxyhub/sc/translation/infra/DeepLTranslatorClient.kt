package kr.galaxyhub.sc.translation.infra

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import io.github.oshai.kotlinlogging.KotlinLogging
import java.time.Duration
import kr.galaxyhub.sc.common.exception.BadRequestException
import kr.galaxyhub.sc.common.exception.InternalServerError
import kr.galaxyhub.sc.common.support.handleConnectError
import kr.galaxyhub.sc.news.domain.Language
import kr.galaxyhub.sc.news.domain.NewsInformation
import kr.galaxyhub.sc.translation.application.TranslatorClient
import kr.galaxyhub.sc.translation.application.TranslatorClientRequest
import kr.galaxyhub.sc.translation.application.TranslatorClientResponse
import kr.galaxyhub.sc.translation.domain.TranslatorProvider
import org.springframework.http.HttpStatus
import org.springframework.web.reactive.function.client.ClientResponse
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono
import reactor.core.publisher.Mono

private val log = KotlinLogging.logger {}

class DeepLTranslatorClient(
    private val webClient: WebClient,
    private val timeoutDuration: Duration,
) : TranslatorClient {

    override fun requestTranslate(request: TranslatorClientRequest): Mono<TranslatorClientResponse> {
        val (newsInformation, content, sourceLanguage, targetLanguage) = request
        return webClient.post()
            .uri("/v2/translate")
            .bodyValue(
                DeepLRequest(
                    sourceLang = sourceLanguage.shortName,
                    targetLang = targetLanguage.shortName,
                    text = toText(newsInformation, content)
                )
            )
            .retrieve()
            .onStatus({ it.isError }) {
                log.info { "DeepL ErrorResponse=${it.bodyToMono<String>().block()}" } // 에러 응답 확인용. 추후 불필요하면 삭제
                handleResponseError(it)
            }
            .bodyToMono<DeepLResponse>()
            .handleConnectError("DeepL 서버와 연결 중 문제가 발생했습니다.")
            .timeout(timeoutDuration, Mono.error {
                log.info { "DeepL 서버의 응답 시간이 초과되었습니다." }
                InternalServerError("DeepL 서버의 응답 시간이 초과되었습니다.")
            })
            .map { it.toResponse(targetLanguage) }
    }

    /**
     * https://www.deepl.com/ko/docs-api/api-access/error-handling
     */
    private fun <T> handleResponseError(clientResponse: ClientResponse): Mono<T> {
        val statusCode = clientResponse.statusCode()
        return when (statusCode.value()) {
            HttpStatus.TOO_MANY_REQUESTS.value() -> {
                Mono.error(BadRequestException("단기간에 너무 많은 요청을 보냈습니다."))
            }

            456 -> {
                log.error { "DeepL 할당량이 초과되었습니다." }
                Mono.error(InternalServerError("할당량이 초과되었습니다. 관리자에게 문의하세요."))
            }

            else -> {
                log.warn { "DeepL 서버에 일시적 문제가 발생했습니다." }
                Mono.error(InternalServerError("번역기 서버에 일시적 문제가 발생했습니다."))
            }
        }
    }

    override fun getProvider(): TranslatorProvider {
        return TranslatorProvider.DEEPL
    }

    private fun toText(newsInformation: NewsInformation, content: String): List<String> {
        val text = mutableListOf<String>()
        text.add(newsInformation.title)
        text.add(newsInformation.excerpt ?: "")
        text.addAll(content.split(System.lineSeparator()))
        return text
    }
}

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
private data class DeepLRequest(
    val sourceLang: String,
    val targetLang: String,
    val text: List<String>,
)

internal data class DeepLResponse(
    val translations: List<DeepLSentenceResponse>,
) {

    fun toResponse(language: Language): TranslatorClientResponse {
        return TranslatorClientResponse(
            newsInformation = toNewsInformation(),
            content = toContent(),
            language = language
        )
    }

    private fun toNewsInformation(): NewsInformation {
        return translations.subList(0, 2)
            .let { NewsInformation(it[0].text, it[1].text) }
    }

    private fun toContent(): String {
        return translations.subList(2, translations.size)
            .joinToString(System.lineSeparator()) { it.text }
    }
}

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
internal data class DeepLSentenceResponse(
    val detectedSourceLanguage: String,
    val text: String,
)
