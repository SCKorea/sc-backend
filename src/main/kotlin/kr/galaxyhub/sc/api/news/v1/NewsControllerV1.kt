package kr.galaxyhub.sc.api.news.v1

import java.util.UUID
import kr.galaxyhub.sc.api.common.ApiResponse
import kr.galaxyhub.sc.api.news.v1.dto.NewsCreateRequest
import kr.galaxyhub.sc.common.support.toUri
import kr.galaxyhub.sc.news.application.NewsCommandService
import kr.galaxyhub.sc.news.application.NewsQueryService
import kr.galaxyhub.sc.news.application.dto.NewsDetailResponse
import kr.galaxyhub.sc.news.application.dto.NewsResponse
import kr.galaxyhub.sc.news.domain.Language
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/news")
class NewsControllerV1(
    private val newsCommandService: NewsCommandService,
    private val newsQueryService: NewsQueryService,
) {

    @GetMapping
    fun findAll():ResponseEntity<ApiResponse<List<NewsResponse>>> {
        val response = newsQueryService.findAll()
        return ResponseEntity.ok()
            .body(ApiResponse.success(response))
    }

    @GetMapping("/{id}")
    fun findDetailById(
        @PathVariable id: UUID,
        @RequestParam language: Language
    ): ResponseEntity<ApiResponse<NewsDetailResponse>> {
        val response = newsQueryService.getDetailByIdAndLanguage(id, language)
        return ResponseEntity.ok()
            .body(ApiResponse.success(response))
    }

    @PostMapping
    fun create(
        @RequestBody request: NewsCreateRequest,
    ): ResponseEntity<ApiResponse<UUID>> {
        val id = newsCommandService.create(request.toCommand())
        return ResponseEntity.created("/api/v1/news/${id}".toUri())
            .body(ApiResponse.success(id))
    }
}
