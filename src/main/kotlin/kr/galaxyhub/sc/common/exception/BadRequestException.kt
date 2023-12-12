package kr.galaxyhub.sc.common.exception

import kr.galaxyhub.sc.common.support.LogLevel
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode

class BadRequestException(
    message: String,
    httpStatus: HttpStatusCode = HttpStatus.BAD_REQUEST,
    logLevel: LogLevel = LogLevel.INFO,
) : GalaxyhubException(message, httpStatus, logLevel)
