package kr.galaxyhub.sc.api.support

import org.springframework.restdocs.payload.FieldDescriptor
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation

open class DocField(
    val descriptor: FieldDescriptor,
) {

    infix fun isOptional(value: Boolean): DocField {
        if (value) descriptor.optional()
        return this
    }

    infix fun means(value: String): DocField {
        descriptor.description(value)
        return this
    }

    infix fun constraint(value: String): DocField {
        descriptor.attributes(RestDocsUtils.constraint(value))
        return this
    }

    infix fun formattedAs(value: String): DocField {
        descriptor.attributes(RestDocsUtils.format(value))
        return this
    }
}

infix fun <T : Enum<T>> String.type(enumFieldType: ENUM<T>): DocField {
    val field = createField(this, JsonFieldType.STRING)
    field formattedAs enumFieldType.enums.joinToString(separator = ", ")
    return field
}

infix fun String.type(docsFieldType: DocsFieldType): DocField {
    val field = createField(this, docsFieldType.type)
    when (docsFieldType) {
        is DATE -> field formattedAs DATE.DEFAULT_FORMAT
        is DATETIME -> field formattedAs DATETIME.DEFAULT_FORMAT
        is ZONEDDATETIME -> field formattedAs ZONEDDATETIME.DEFAULT_FORMAT
        else -> {}
    }
    return field
}

private fun createField(
    value: String,
    type: JsonFieldType,
): DocField {
    val descriptor = PayloadDocumentation
        .fieldWithPath(value)
        .type(type)
        .description("")
        .attributes(*RestDocsUtils.getAllEmptyFormats())
    return DocField(descriptor)
}

