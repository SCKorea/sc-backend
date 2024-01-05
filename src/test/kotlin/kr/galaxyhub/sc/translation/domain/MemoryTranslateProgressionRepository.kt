package kr.galaxyhub.sc.translation.domain

import java.util.UUID

class MemoryTranslateProgressionRepository(
    private val memory: MutableMap<UUID, TranslateProgression> = mutableMapOf(),
) : TranslateProgressionRepository {

    fun clear() = memory.clear()

    override fun save(translateProgression: TranslateProgression): TranslateProgression {
        memory[translateProgression.id] = translateProgression
        return translateProgression
    }

    override fun findById(translateProgressionId: UUID): TranslateProgression? {
        return memory[translateProgressionId]
    }
}
