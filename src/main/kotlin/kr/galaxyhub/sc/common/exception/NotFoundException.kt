package kr.galaxyhub.sc.common.exception

import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode

class NotFoundException(
    message: String,
    httpStatus: HttpStatusCode = HttpStatus.NOT_FOUND,
) : GalaxyhubException(message, httpStatus)
