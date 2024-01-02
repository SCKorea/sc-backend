package kr.galaxyhub.sc.news.domain

import java.util.UUID
import kr.galaxyhub.sc.common.exception.NotFoundException
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.Repository
import org.springframework.data.repository.query.Param

fun NewsRepository.getFetchByIdAndLanguage(id: UUID, language: Language) = findFetchByIdAndLanguage(id, language)
    ?: throw NotFoundException("식별자와 언어에 대한 뉴스를 찾을 수 없습니다. id=${id}, language=${language}")

fun NewsRepository.getOrThrow(id: UUID) = findById(id)
    ?: throw NotFoundException("식별자에 대한 뉴스를 찾을 수 없습니다. id=${id}")

interface NewsRepository : Repository<News, UUID> {

    fun save(news: News): News

    fun findAll(): List<News>

    fun findByOriginId(originId: Long): News?

    fun findById(id: UUID): News?

    @Query(
        """
        select n
        from News n
        inner join fetch n.contents nc 
        where n.id = :id and nc.language = :language
    """
    )
    fun findFetchByIdAndLanguage(@Param("id") id: UUID, @Param("language") language: Language): News?
}
