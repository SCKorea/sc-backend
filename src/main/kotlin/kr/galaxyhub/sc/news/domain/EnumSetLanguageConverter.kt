package kr.galaxyhub.sc.news.domain

import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter
import java.util.EnumSet

@Converter
class EnumSetLanguageConverter : AttributeConverter<EnumSet<Language>, String> {

    override fun convertToDatabaseColumn(attribute: EnumSet<Language>): String {
        return attribute.joinToString(separator = SEPARATOR)
    }

    override fun convertToEntityAttribute(dbData: String?): EnumSet<Language> {
        return if (dbData.isNullOrBlank()) {
            EnumSet.noneOf(Language::class.java)
        } else {
            dbData.split(SEPARATOR)
                .map { Language.valueOf(it) }
                .toCollection(EnumSet.noneOf(Language::class.java))
        }
    }

    companion object {

        private const val SEPARATOR = ","
    }
}
