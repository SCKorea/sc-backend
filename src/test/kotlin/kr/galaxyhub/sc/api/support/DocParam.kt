package kr.galaxyhub.sc.api.support

import org.springframework.restdocs.request.ParameterDescriptor
import org.springframework.restdocs.request.RequestDocumentation

open class DocParam(
    val descriptor: ParameterDescriptor,
) {

    infix fun isOptional(value: Boolean): DocParam {
        if (value) descriptor.optional()
        return this
    }

    infix fun constraint(value: String): DocParam {
        descriptor.attributes(RestDocsUtils.constraint(value))
        return this
    }

    infix fun formattedAs(value: String): DocParam {
        descriptor.attributes(RestDocsUtils.format(value))
        return this
    }

    infix fun <T : Enum<T>> formattedAs(enumFieldType: ENUM<T>): DocParam {
        return formattedAs(enumFieldType.enums.joinToString(separator = ", "))
    }
}

infix fun String.pathMeans(description: String): DocParam {
    return createField(this, description)
}

private fun createField(
    value: String,
    description: String,
): DocParam {
    val descriptor = RequestDocumentation
        .parameterWithName(value)
        .description(description)
        .attributes(*RestDocsUtils.getAllEmptyAttributes())
    return DocParam(descriptor)
}
