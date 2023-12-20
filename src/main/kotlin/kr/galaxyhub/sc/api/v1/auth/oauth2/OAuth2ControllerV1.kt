package kr.galaxyhub.sc.api.v1.auth.oauth2

import kr.galaxyhub.sc.api.common.ApiResponse
import kr.galaxyhub.sc.auth.application.OAuth2FacadeService
import kr.galaxyhub.sc.auth.application.dto.LoginResponse
import kr.galaxyhub.sc.member.domain.SocialType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/auth/oauth2")
class OAuth2ControllerV1(
    val oAuth2FacadeService: OAuth2FacadeService,
) {

    @GetMapping("/login")
    fun login(
        @RequestParam code: String,
        @RequestParam socialType: SocialType,
    ): ResponseEntity<ApiResponse<LoginResponse>> {
        val response = oAuth2FacadeService.login(code, socialType)
        return ResponseEntity.ok()
            .body(ApiResponse.success(response))
    }
}
