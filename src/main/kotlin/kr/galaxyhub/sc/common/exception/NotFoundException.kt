package kr.galaxyhub.sc.common.exception

import kr.galaxyhub.sc.common.support.LogLevel
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode

class NotFoundException(
    message: String,
    httpStatus: HttpStatusCode = HttpStatus.NOT_FOUND,
    logLevel: LogLevel = LogLevel.DEBUG,
) : GalaxyhubException(message, httpStatus, logLevel)
