package kr.galaxyhub.sc.translation.domain

import java.util.UUID
import kr.galaxyhub.sc.common.exception.NotFoundException
import org.springframework.data.repository.Repository

fun TranslationProgressionRepository.getOrThrow(translateProgressionId: UUID) = findById(translateProgressionId)
    ?: throw NotFoundException("식별자에 대한 번역 진행 상황을 찾을 수 없습니다. id=$translateProgressionId")

interface TranslationProgressionRepository : Repository<TranslateProgression, UUID> {

    fun save(translateProgression: TranslateProgression): TranslateProgression

    fun findById(translateProgressionId: UUID): TranslateProgression?
}
