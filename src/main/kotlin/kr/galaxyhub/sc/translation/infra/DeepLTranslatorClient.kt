package kr.galaxyhub.sc.translation.infra

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import io.github.oshai.kotlinlogging.KotlinLogging
import java.util.UUID
import kr.galaxyhub.sc.common.exception.BadRequestException
import kr.galaxyhub.sc.common.exception.InternalServerError
import kr.galaxyhub.sc.common.support.handleConnectError
import kr.galaxyhub.sc.news.domain.Content
import kr.galaxyhub.sc.news.domain.Language
import kr.galaxyhub.sc.news.domain.NewsInformation
import kr.galaxyhub.sc.translation.domain.TranslatorClient
import kr.galaxyhub.sc.translation.domain.TranslatorProvider
import org.springframework.http.HttpStatus
import org.springframework.web.reactive.function.client.ClientResponse
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono
import reactor.core.publisher.Mono

private val log = KotlinLogging.logger {}

class DeepLTranslatorClient(
    private val webClient: WebClient,
) : TranslatorClient {

    override fun requestTranslate(content: Content, targetLanguage: Language): Mono<Content> {
        return webClient.post()
            .uri("/v2/translate")
            .bodyValue(DeepLRequest(content.language.shortName, targetLanguage.shortName, content.toText()))
            .retrieve()
            .onStatus({ it.isError }) {
                log.info { "DeepL ErrorResponse=${it.bodyToMono<String>().block()}" } // 에러 응답 확인용. 추후 불필요하면 삭제
                handleResponseError(it)
            }
            .bodyToMono<DeepLResponse>()
            .handleConnectError("DeepL 서버와 연결 중 문제가 발생했습니다.")
            .map { it.toContent(content.newsId, targetLanguage) }
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

    private fun Content.toText(): List<String> {
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

    fun toContent(newsId: UUID, language: Language): Content {
        val newsInformation = toNewsInformation()
        return Content(
            newsId = newsId,
            newsInformation = newsInformation,
            language = language,
            content = toContentString()
        )
    }

    private fun toNewsInformation(): NewsInformation {
        return translations.subList(0, 2)
            .let { NewsInformation(it[0].text, it[1].text) }
    }

    private fun toContentString(): String {
        return translations.subList(2, translations.size)
            .joinToString(System.lineSeparator()) { it.text }
    }
}

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
internal data class DeepLSentenceResponse(
    val detectedSourceLanguage: String,
    val text: String,
)
