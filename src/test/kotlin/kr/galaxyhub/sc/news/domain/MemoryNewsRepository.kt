package kr.galaxyhub.sc.news.domain

import java.util.UUID

class MemoryNewsRepository(
    private val memory: MutableMap<UUID, News> = mutableMapOf(),
) : NewsRepository {

    fun clear() = memory.clear()

    override fun save(news: News): News {
        memory[news.id] = news
        return news
    }

    override fun findAll(): List<News> = memory.values.toList()

    override fun findByOriginId(originId: Long): News? = memory.values.firstOrNull { it.originId == originId }

    override fun findById(id: UUID): News? = memory[id]

    override fun findFetchByIdAndLanguage(id: UUID, language: Language): News? {
        return memory[id]?.takeIf { it.supportLanguages.contains(language) }
    }
}
