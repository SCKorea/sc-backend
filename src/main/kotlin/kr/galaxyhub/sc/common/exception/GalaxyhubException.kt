package kr.galaxyhub.sc.common.exception

import kr.galaxyhub.sc.common.support.LogLevel
import org.springframework.http.HttpStatusCode

open class GalaxyhubException(
    message: String,
    val httpStatus: HttpStatusCode,
    val logLevel: LogLevel,
) : RuntimeException(message)
