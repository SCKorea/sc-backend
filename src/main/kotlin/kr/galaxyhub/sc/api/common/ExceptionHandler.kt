package kr.galaxyhub.sc.api.common

import io.github.oshai.kotlinlogging.KotlinLogging
import jakarta.servlet.http.HttpServletRequest
import kr.galaxyhub.sc.common.exception.GalaxyhubException
import kr.galaxyhub.sc.common.support.LogLevel
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MissingServletRequestParameterException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

private val log = KotlinLogging.logger {}

@RestControllerAdvice
class ExceptionHandler : ResponseEntityExceptionHandler() {

    override fun handleMissingServletRequestParameter(
        ex: MissingServletRequestParameterException,
        headers: HttpHeaders,
        status: HttpStatusCode,
        request: WebRequest,
    ): ResponseEntity<Any> {
        return ResponseEntity(
            ApiResponse("쿼리 파라미터에 누락된 값이 있습니다.", ex.missingParameters()),
            HttpStatus.BAD_REQUEST
        )
    }

    private fun MissingServletRequestParameterException.missingParameters(): String {
        return this.detailMessageArguments.contentToString()
    }

    @ExceptionHandler(GalaxyhubException::class)
    fun handleGalaxyhubException(
        e: GalaxyhubException,
        request: HttpServletRequest,
    ): ResponseEntity<ApiResponse<Unit>> {
        when (e.logLevel) {
            LogLevel.ERROR -> log.error(e) { "[🔴ERROR] - (${request.method} ${request.requestURI})" }
            LogLevel.WARN -> log.warn(e) { "[🟠WARN] - (${request.method} ${request.requestURI})" }
            LogLevel.INFO -> log.info(e) { "[🔵INFO] - (${request.method} ${request.requestURI})" }
            LogLevel.DEBUG -> log.debug(e) { "[🟢DEBUG] - (${request.method} ${request.requestURI})" }
        }
        return ResponseEntity(ApiResponse.error(e.message!!), e.httpStatus)
    }

    @ExceptionHandler(Exception::class)
    fun handleException(e: Exception, request: HttpServletRequest): ResponseEntity<ApiResponse<Unit>> {
        log.error(e) { "[🔴ERROR] - (${request.method} ${request.requestURI})" }
        return ResponseEntity(DEFAULT_ERROR, HttpStatus.INTERNAL_SERVER_ERROR)
    }

    companion object {

        private val DEFAULT_ERROR = ApiResponse.error("서버 내부에 알 수 없는 문제가 발생했습니다.")
    }
}

