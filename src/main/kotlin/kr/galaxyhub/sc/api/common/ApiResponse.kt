package kr.galaxyhub.sc.api.common

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonInclude.Include

@JsonInclude(Include.NON_NULL)
class ApiResponse<T>(
    val status: Int,
    val message: String? = null,
    val data: T?,
) {

    companion object {

        fun error(statusCode: Int, message: String): ApiResponse<Unit> {
            return ApiResponse(status = statusCode, message = message, data = Unit)
        }

        fun <T> ok(data: T): ApiResponse<T> {
            return ApiResponse(status = 200, message = null, data = data)
        }

        fun <T> created(data: T): ApiResponse<T> {
            return ApiResponse(status = 201, message = null, data = data)
        }

        fun <T> noContent(data: T): ApiResponse<T> {
            return ApiResponse(status = 204, message = null, data = data)
        }
    }
}
