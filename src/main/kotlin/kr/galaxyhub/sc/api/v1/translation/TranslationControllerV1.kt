package kr.galaxyhub.sc.api.v1.translation

import java.util.UUID
import kr.galaxyhub.sc.api.common.ApiResponse
import kr.galaxyhub.sc.api.v1.translation.dto.TranslationRequest
import kr.galaxyhub.sc.common.support.toUri
import kr.galaxyhub.sc.translation.application.TranslationCommandService
import kr.galaxyhub.sc.translation.application.TranslationQueryService
import kr.galaxyhub.sc.translation.application.dto.TranslationCommand
import kr.galaxyhub.sc.translation.application.dto.TranslationResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/translation")
class TranslationControllerV1(
    private val translationCommandService: TranslationCommandService,
    private val translationQueryService: TranslationQueryService,
) {

    @PostMapping("/{newsId}")
    fun translate(
        @PathVariable newsId: UUID,
        @RequestBody request: TranslationRequest,
    ): ResponseEntity<ApiResponse<UUID>> {
        val command = TranslationCommand(newsId, request.destinationLanguage)
        val translateProgressionId = translationCommandService.translate(command)
        return ResponseEntity.created("/api/v1/translation/${translateProgressionId}".toUri())
            .body(ApiResponse.success(translateProgressionId))
    }

    @GetMapping("/{translateProgressionId}")
    fun findById(
        @PathVariable translateProgressionId: UUID,
    ): ResponseEntity<ApiResponse<TranslationResponse>> {
        val response = translationQueryService.findById(translateProgressionId)
        return ResponseEntity.ok()
            .body(ApiResponse.success(response))
    }
}
