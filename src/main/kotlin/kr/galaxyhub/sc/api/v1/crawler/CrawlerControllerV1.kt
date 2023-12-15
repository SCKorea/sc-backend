package kr.galaxyhub.sc.api.v1.crawler

import java.util.UUID
import kr.galaxyhub.sc.api.common.ApiResponse
import kr.galaxyhub.sc.api.v1.crawler.dto.NewsCrawlingRequest
import kr.galaxyhub.sc.common.support.toUri
import kr.galaxyhub.sc.crawler.application.CrawlerCommandService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * TODO: 지금은 동기적으로 크롤링이 수행되지만, DB에 크롤링한 요청을 저장하고, 크롤링 상태를 나타내게 하는 방법 고려
 */
@RestController
@RequestMapping("/api/v1/crawler")
class CrawlerControllerV1(
    private val crawlerCommandService: CrawlerCommandService,
) {

    @PostMapping
    fun crawling(
        @RequestBody request: NewsCrawlingRequest,
    ): ResponseEntity<ApiResponse<UUID>> {
        val newsId = crawlerCommandService.crawling(request.url)
        return ResponseEntity.created("/api/v1/news/${newsId}".toUri())
            .body(ApiResponse.success(newsId))
    }
}
