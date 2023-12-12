package kr.galaxyhub.sc.api.common

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonInclude.Include

@JsonInclude(Include.NON_NULL)
class ApiResponse<T>(
    val message: String? = null,
    val data: T,
) {

    companion object {

        fun error(message: String): ApiResponse<Unit> {
            return ApiResponse(message, Unit)
        }

        fun <T> success(data: T): ApiResponse<T> {
            return ApiResponse(data = data)
        }
    }
}
