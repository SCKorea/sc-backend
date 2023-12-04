package kr.galaxyhub.sc.news.domain

import io.kotest.assertions.assertSoftly
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.inspectors.forAll
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import java.util.EnumSet

class EnumSetLanguageConverterTest : DescribeSpec({

    val enumSetLanguageConverter = EnumSetLanguageConverter()

    describe("convertToDatabaseColumn") {
        context("요소가 한 개 이면") {
            val attribute = EnumSet.of(Language.ENGLISH)

            it(",가 없는 문자열을 반환한다.") {
                val expect = enumSetLanguageConverter.convertToDatabaseColumn(attribute)

                expect shouldBe "ENGLISH"
            }
        }

        context("요소가 여러 개 이면") {
            val attribute = EnumSet.of(Language.ENGLISH, Language.KOREAN)

            it(",가 있는 문자열을 반환한다.") {
                val expect = enumSetLanguageConverter.convertToDatabaseColumn(attribute)

                assertSoftly {
                    expect shouldContain ","
                    expect shouldContain "ENGLISH"
                    expect shouldContain "KOREAN"
                }
            }
        }

        context("요소가 없으면") {
            val attribute = EnumSet.noneOf(Language::class.java)

            it("빈 문자열을 반환한다.") {
                val expect = enumSetLanguageConverter.convertToDatabaseColumn(attribute)

                expect shouldBe ""
            }
        }
    }

    describe("convertToEntityAttribute") {
        context(",가 없는 문자열이면") {
            val dbData = "ENGLISH"

            it("한 개의 요소를 반환한다.") {
                val expect = enumSetLanguageConverter.convertToEntityAttribute(dbData)

                assertSoftly {
                    expect shouldHaveSize 1
                    expect shouldContainExactly setOf(Language.ENGLISH)
                }
            }
        }

        context(",가 있는 문자열이면") {
            val dbData = "ENGLISH,KOREAN"

            it("여러 개의 요소를 반환한다.") {
                val expect = enumSetLanguageConverter.convertToEntityAttribute(dbData)

                assertSoftly {
                    expect shouldHaveSize 2
                    expect shouldContainExactly setOf(Language.ENGLISH, Language.KOREAN)
                }
            }
        }

        context("null 또는 빈 문자열이면") {
            val dbDate = listOf(null, "", " ", "\t", "\n")

            it("비어 있는 요소를 반환한다.") {
                dbDate.forAll {
                    val expect = enumSetLanguageConverter.convertToEntityAttribute(it)

                    expect shouldHaveSize 0
                }
            }
        }
    }
})
