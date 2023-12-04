package kr.galaxyhub.sc.news.fixture

import kr.galaxyhub.sc.news.domain.Content
import kr.galaxyhub.sc.news.domain.Language
import kr.galaxyhub.sc.news.domain.NewsInformation

object ContentFixture {

    fun of(language: Language): Content = when (language) {
        Language.ENGLISH -> Content(
            newsInformation = NewsInformation(
                title = "Star Citizen Live",
                excerpt = "You asked. We're answering! Join us today for a live Q&A show with the Vehicle Gameplay team."
            ),
            language = language,
            content = "blah blah",
        )

        Language.KOREAN -> Content(
            newsInformation = NewsInformation(
                title = "스타 시티즌 뉴스",
                excerpt = "물어보셨죠? 저희가 답해드리겠습니다! 지금 바로 차량 게임플레이 팀과 함께하는 라이브 Q&A 쇼에 참여하세요."
            ),
            language = language,
            content = "어쩌구 저쩌구",
        )
    }
}
