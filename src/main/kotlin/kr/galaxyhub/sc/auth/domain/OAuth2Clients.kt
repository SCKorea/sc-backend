package kr.galaxyhub.sc.auth.domain

import java.util.EnumMap
import kr.galaxyhub.sc.common.exception.BadRequestException
import kr.galaxyhub.sc.member.domain.SocialType

class OAuth2Clients(
    private val oAuth2Clients: Map<SocialType, OAuth2Client>,
) {

    fun getClient(socialType: SocialType): OAuth2Client {
        return oAuth2Clients[socialType]
            ?: throw BadRequestException("해당 OAuth2 서비스 제공자는 제공되지 않습니다. socialType=$socialType")
    }

    companion object {

        fun from(oAuth2Clients: List<OAuth2Client>): OAuth2Clients {
            val oAuth2ClientMap = EnumMap<SocialType, OAuth2Client>(SocialType::class.java)
            oAuth2Clients.forEach {
                oAuth2ClientMap[it.getSocialType()] = it
            }
            return OAuth2Clients(oAuth2ClientMap)
        }
    }
}
