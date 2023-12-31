package kr.galaxyhub.sc.news.application

import java.util.UUID
import kr.galaxyhub.sc.news.application.dto.NewsDetailResponse
import kr.galaxyhub.sc.news.application.dto.NewsResponse
import kr.galaxyhub.sc.news.domain.Language
import kr.galaxyhub.sc.news.domain.NewsRepository
import kr.galaxyhub.sc.news.domain.getFetchByIdAndLanguage
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class NewsQueryService(
    private val newsRepository: NewsRepository,
) {

    fun findAll(): List<NewsResponse> {
        return newsRepository.findAll()
            .map { NewsResponse.from(it) }
    }

    fun getDetailByIdAndLanguage(id: UUID, language: Language): NewsDetailResponse {
        return newsRepository.getFetchByIdAndLanguage(id, language)
            .let {
                val content = it.getContentByLanguage(language)
                NewsDetailResponse.of(it, content)
            }
    }
}
