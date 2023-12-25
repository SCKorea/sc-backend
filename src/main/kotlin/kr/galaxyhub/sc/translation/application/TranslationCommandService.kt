package kr.galaxyhub.sc.translation.application

import java.util.UUID
import kr.galaxyhub.sc.translation.application.dto.TranslationCommand
import kr.galaxyhub.sc.translation.domain.TranslateProgression
import kr.galaxyhub.sc.translation.domain.TranslationProgressionRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class TranslationCommandService(
    private val translationProgressionRepository: TranslationProgressionRepository,
) {

    fun translate(command: TranslationCommand): UUID {
        val (newsId, sourceLanguage, targetLanguage, translatorProvider) = command
        val translateProgression = TranslateProgression(newsId, sourceLanguage, targetLanguage, translatorProvider)
        translationProgressionRepository.save(translateProgression)
        return translateProgression.id
    }
}