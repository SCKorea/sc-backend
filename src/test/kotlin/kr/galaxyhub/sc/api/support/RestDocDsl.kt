package kr.galaxyhub.sc.api.support

import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation
import org.springframework.restdocs.operation.preprocess.OperationRequestPreprocessor
import org.springframework.restdocs.operation.preprocess.OperationResponsePreprocessor
import org.springframework.restdocs.operation.preprocess.Preprocessors
import org.springframework.restdocs.payload.PayloadDocumentation
import org.springframework.restdocs.payload.RequestFieldsSnippet
import org.springframework.restdocs.payload.ResponseFieldsSnippet
import org.springframework.restdocs.request.PathParametersSnippet
import org.springframework.restdocs.request.QueryParametersSnippet
import org.springframework.restdocs.request.RequestDocumentation
import org.springframework.test.web.servlet.ResultActionsDsl

class RestDocDsl {

    private var pathParametersSnippet: PathParametersSnippet? = null

    private var requestFieldsSnippet: RequestFieldsSnippet? = null

    private var responseFieldsSnippet: ResponseFieldsSnippet? = null

    private var queryParametersSnippet: QueryParametersSnippet? = null

    fun pathParameters(vararg params: DocParam) {
        pathParametersSnippet = RequestDocumentation.pathParameters(params.map { it.descriptor })
    }

    fun requestBody(vararg fields: DocField) {
        requestFieldsSnippet = PayloadDocumentation.requestFields(fields.map { it.descriptor })
    }

    fun responseBody(vararg fields: DocField) {
        responseFieldsSnippet = PayloadDocumentation.responseFields(fields.map { it.descriptor })
    }

    fun queryParameters(vararg params: DocParam) {
        queryParametersSnippet = RequestDocumentation.queryParameters(params.map { it.descriptor })
    }

    internal fun perform(identifier: String, resultActionDsl: ResultActionsDsl): ResultActionsDsl {
        return resultActionDsl.andDo {
            handle(
                MockMvcRestDocumentation.document(
                    identifier,
                    preprocessRequest,
                    preprocessResponse,
                    *listOfNotNull(
                        pathParametersSnippet,
                        requestFieldsSnippet,
                        responseFieldsSnippet,
                        queryParametersSnippet
                    ).toTypedArray()
                )
            )
        }
    }

    companion object {

        val preprocessRequest: OperationRequestPreprocessor = Preprocessors.preprocessRequest(
            Preprocessors.prettyPrint(),
        )

        val preprocessResponse: OperationResponsePreprocessor = Preprocessors.preprocessResponse(
            Preprocessors.prettyPrint(),
        )
    }
}

