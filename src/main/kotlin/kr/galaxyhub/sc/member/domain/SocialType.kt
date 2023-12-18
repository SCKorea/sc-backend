package kr.galaxyhub.sc.member.domain

import java.util.Locale

enum class SocialType {
    LOCAL,
    DISCORD,
    ;

    companion object {

        fun from(lowerCase: String): SocialType {
            return SocialType.valueOf(lowerCase.uppercase(Locale.getDefault()))
        }
    }
}
