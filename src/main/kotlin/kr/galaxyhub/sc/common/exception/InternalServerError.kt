package kr.galaxyhub.sc.common.exception

import kr.galaxyhub.sc.common.support.LogLevel
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode

class InternalServerError(
    message: String,
    httpStatus: HttpStatusCode = HttpStatus.INTERNAL_SERVER_ERROR,
    logLevel: LogLevel = LogLevel.WARN,
) : GalaxyhubException(message, httpStatus, logLevel)
