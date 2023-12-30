package kr.galaxyhub.sc.common.exception

import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode

class InternalServerError(
    message: String,
    httpStatus: HttpStatusCode = HttpStatus.INTERNAL_SERVER_ERROR,
) : GalaxyhubException(message, httpStatus)
