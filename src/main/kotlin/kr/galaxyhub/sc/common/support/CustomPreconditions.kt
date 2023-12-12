package kr.galaxyhub.sc.common.support

import kr.galaxyhub.sc.common.exception.BadRequestException

inline fun validate(value: Boolean, lazyMessage: () -> Any) {
    if (value) {
        val message = lazyMessage()
        throw BadRequestException(message.toString())
    }
}
