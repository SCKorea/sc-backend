package kr.galaxyhub.sc.common.exception

import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode

class UnauthorizedException(
    message: String,
    httpStatus: HttpStatusCode = HttpStatus.UNAUTHORIZED,
) : GalaxyhubException(message, httpStatus)
