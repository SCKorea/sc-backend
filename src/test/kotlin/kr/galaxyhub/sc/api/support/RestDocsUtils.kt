package kr.galaxyhub.sc.api.support

import org.springframework.restdocs.snippet.Attributes

class RestDocsUtils {

    companion object {

        private val EMPTY_FORMAT = Attributes.key("format").value("")
        private val EMPTY_CONSTRAINT = Attributes.key("constraint").value("")
        private val FORMAT_BUILDER = Attributes.key("format")
        private val CONSTRAINT_BUILDER = Attributes.key("constraint")

        fun emptyFormat(): Attributes.Attribute = EMPTY_FORMAT

        fun format(value: String): Attributes.Attribute = FORMAT_BUILDER.value(value)

        fun emptyConstraint(): Attributes.Attribute = EMPTY_CONSTRAINT

        fun constraint(value: String): Attributes.Attribute = CONSTRAINT_BUILDER.value(value)

        fun getAllEmptyAttributes(): Array<Attributes.Attribute> {
            return arrayOf(EMPTY_FORMAT, EMPTY_CONSTRAINT)
        }
    }
}
