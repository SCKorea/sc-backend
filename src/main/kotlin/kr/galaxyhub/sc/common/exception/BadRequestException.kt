package kr.galaxyhub.sc.common.exception

import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode

class BadRequestException(
    message: String,
    httpStatus: HttpStatusCode = HttpStatus.BAD_REQUEST,
) : GalaxyhubException(message, httpStatus)
