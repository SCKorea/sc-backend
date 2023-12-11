package kr.galaxyhub.sc.api.support

import kotlin.reflect.KClass
import org.springframework.restdocs.payload.JsonFieldType

sealed class DocsFieldType(
    val type: JsonFieldType,
)

data object ARRAY : DocsFieldType(JsonFieldType.ARRAY)
data object BOOLEAN : DocsFieldType(JsonFieldType.BOOLEAN)
data object OBJECT : DocsFieldType(JsonFieldType.OBJECT)
data object NUMBER : DocsFieldType(JsonFieldType.NUMBER)
data object NULL : DocsFieldType(JsonFieldType.NULL)
data object STRING : DocsFieldType(JsonFieldType.STRING)
data object ANY : DocsFieldType(JsonFieldType.VARIES)
data object DATE : DocsFieldType(JsonFieldType.STRING) {
    const val DEFAULT_FORMAT = "2023-12-11"
}
data object DATETIME : DocsFieldType(JsonFieldType.STRING) {
    const val DEFAULT_FORMAT = "2023-12-11T10:15:30"
}
data object ZONEDDATETIME : DocsFieldType(JsonFieldType.STRING) {
    const val DEFAULT_FORMAT = "2023-12-11T10:15:30+09:00"
}
data class ENUM<T : Enum<T>>(val enums: Collection<T>) : DocsFieldType(JsonFieldType.STRING) {
    constructor(clazz: KClass<T>) : this(clazz.java.enumConstants.asList())
}
