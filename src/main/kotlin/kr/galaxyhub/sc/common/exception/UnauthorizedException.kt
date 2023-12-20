package kr.galaxyhub.sc.common.exception

import kr.galaxyhub.sc.common.support.LogLevel
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode

class UnauthorizedException(
    message: String,
    httpStatus: HttpStatusCode = HttpStatus.UNAUTHORIZED,
    logLevel: LogLevel = LogLevel.INFO,
) : GalaxyhubException(message, httpStatus, logLevel)
