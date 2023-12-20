package kr.galaxyhub.sc.auth.application

import kr.galaxyhub.sc.auth.application.dto.LoginResponse
import kr.galaxyhub.sc.auth.domain.OAuth2Clients
import kr.galaxyhub.sc.member.domain.SocialType
import org.springframework.stereotype.Service

/**
 * 외부 API를 호출하는 서비스이기 때문에 Transcational 어노테이션을 사용하지 않습니다.
 * OAuth2Clients에서 OAuth2 서버에 API 호출을 하기 때문에, 해당 클래스를 만들어 사용합니다. -> 트랜잭션 범위 최소화 목적
 */
@Service
class OAuth2FacadeService(
    private val oAuth2Clients: OAuth2Clients,
    private val oAuth2CommandService: OAuth2CommandService,
) {

    fun login(code: String, socialType: SocialType): LoginResponse {
        val client = oAuth2Clients.getClient(socialType)
        val accessToken = client.getAccessToken(code)
        val userInfo = client.getUserInfo(accessToken)
        return oAuth2CommandService.login(userInfo)
    }
}
