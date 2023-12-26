package kr.galaxyhub.sc.translation.domain

import java.util.EnumMap
import kr.galaxyhub.sc.common.exception.BadRequestException

class TranslatorClients(
    private val translatorClients: Map<TranslatorProvider, TranslatorClient>,
) {

    fun getClient(translatorProvider: TranslatorProvider): TranslatorClient {
        return translatorClients[translatorProvider]
            ?: throw BadRequestException("해당 번역 서비스 제공자는 제공되지 않습니다. translatorProvider=$translatorProvider")
    }

    companion object {

        fun from(translatorClients: List<TranslatorClient>): TranslatorClients {
            val translatorClientMap = EnumMap<TranslatorProvider, TranslatorClient>(TranslatorProvider::class.java)
            translatorClients.forEach {
                translatorClientMap[it.getProvider()] = it
            }
            return TranslatorClients(translatorClientMap)
        }
    }
}
