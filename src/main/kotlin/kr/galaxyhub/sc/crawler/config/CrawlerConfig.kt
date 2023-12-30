package kr.galaxyhub.sc.crawler.config

import com.fasterxml.jackson.databind.ObjectMapper
import kr.galaxyhub.sc.crawler.application.Crawlers
import kr.galaxyhub.sc.crawler.domain.DocumentProvider
import kr.galaxyhub.sc.crawler.domain.HtmlParser
import kr.galaxyhub.sc.crawler.infra.EngineeringCrawler
import kr.galaxyhub.sc.crawler.infra.parser.MarkdownHtmlParser
import kr.galaxyhub.sc.crawler.infra.parser.PlainHtmlParser
import kr.galaxyhub.sc.crawler.infra.provider.JsoupDocumentProvider
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class CrawlerConfig(
    private val objectMapper: ObjectMapper,
) {

    @Bean
    fun documentProvider(): DocumentProvider {
        return JsoupDocumentProvider()
    }

    @Bean
    fun plainHtmlParser(): HtmlParser {
        return PlainHtmlParser()
    }

    @Bean
    fun markdownHtmlParser(): HtmlParser {
        return MarkdownHtmlParser()
    }

    @Bean
    fun crawlers(): Crawlers {
        return Crawlers(
            listOf(
                engineeringCrawler(),
            )
        )
    }

    @Bean
    fun engineeringCrawler(): EngineeringCrawler {
        return EngineeringCrawler(
            objectMapper = objectMapper,
            documentProvider = documentProvider(),
            contentParser = markdownHtmlParser(),
            introductionParser = plainHtmlParser(),
        )
    }
}
