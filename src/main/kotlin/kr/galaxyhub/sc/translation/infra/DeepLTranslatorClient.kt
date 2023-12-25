package kr.galaxyhub.sc.translation.infra

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import io.github.oshai.kotlinlogging.KotlinLogging
import kr.galaxyhub.sc.common.exception.BadRequestException
import kr.galaxyhub.sc.common.exception.InternalServerError
import kr.galaxyhub.sc.news.domain.Content
import kr.galaxyhub.sc.news.domain.Language
import kr.galaxyhub.sc.news.domain.News
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
            .onStatus({ it.is4xxClientError || it.is5xxServerError }) {
                log.info { "DeepL ErrorResponse=${it.bodyToMono<String>().block()}" } // 에러 응답 확인용. 추후 불필요하면 삭제
                throw handleError(it)
            }
            .bodyToMono<DeepLResponse>()
            .map { it.toContent(content.news, targetLanguage) }
    }

    /**
     * https://www.deepl.com/ko/docs-api/api-access/error-handling
     */
    private fun handleError(clientResponse: ClientResponse): Exception {
        val statusCode = clientResponse.statusCode()
        return when(statusCode.value()) {
            HttpStatus.TOO_MANY_REQUESTS.value() -> {
                BadRequestException("단기간에 너무 많은 요청을 보냈습니다.")
            }
            456 -> {
                log.error { "DeepL 할당량이 초과되었습니다." }
                InternalServerError("할당량이 초과되었습니다. 관리자에게 문의하세요.")
            }
            else -> {
                log.warn { "DeepL 서버에 일시적 문제가 발생했습니다." }
                InternalServerError("번역기 서버에 일시적 문제가 발생했습니다.")
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

private data class DeepLResponse(
    val translations: List<DeepLSentenceResponse>,
) {

    fun toContent(news: News, language: Language): Content {
        val newsInformation = toNewsInformation()
        return Content(news, newsInformation, language, toContentString())
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
private data class DeepLSentenceResponse(
    val detectedSourceLanguage: String,
    val text: String,
)