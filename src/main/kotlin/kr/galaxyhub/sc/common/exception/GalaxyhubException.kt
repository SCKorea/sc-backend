package kr.galaxyhub.sc.common.exception

import org.springframework.http.HttpStatusCode

sealed class GalaxyhubException(
    message: String,
    val httpStatus: HttpStatusCode,
) : RuntimeException(message)
