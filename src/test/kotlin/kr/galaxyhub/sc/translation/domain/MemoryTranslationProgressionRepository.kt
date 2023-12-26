package kr.galaxyhub.sc.translation.domain

import java.util.UUID

class MemoryTranslationProgressionRepository(
    private val memory: MutableMap<UUID, TranslateProgression> = mutableMapOf(),
) : TranslationProgressionRepository {

    fun clear() = memory.clear()

    override fun save(translateProgression: TranslateProgression): TranslateProgression {
        memory[translateProgression.id] = translateProgression
        return translateProgression
    }

    override fun findById(translateProgressionId: UUID): TranslateProgression? {
        return memory[translateProgressionId]
    }
}
