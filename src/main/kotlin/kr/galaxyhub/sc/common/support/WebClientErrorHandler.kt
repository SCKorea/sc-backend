package kr.galaxyhub.sc.common.support

import io.github.oshai.kotlinlogging.KotlinLogging
import kr.galaxyhub.sc.common.exception.GalaxyhubException
import kr.galaxyhub.sc.common.exception.InternalServerError
import reactor.core.publisher.Mono

private val log = KotlinLogging.logger {}

fun <T> Mono<T>.handleConnectError(
    exMessage: String = "외부 서버와 연결 중 문제가 발생했습니다.",
): Mono<T> {
    return onErrorResume({ it !is GalaxyhubException }) {
        log.error(it) { exMessage }
        Mono.error(InternalServerError(exMessage))
    }
}
