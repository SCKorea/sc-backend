package kr.galaxyhub.sc.crawler.application

import io.github.oshai.kotlinlogging.KotlinLogging
import org.aspectj.lang.JoinPoint
import org.aspectj.lang.annotation.AfterThrowing
import org.aspectj.lang.annotation.Aspect
import org.springframework.stereotype.Component

private val log = KotlinLogging.logger {}

@Component
@Aspect
class CrawlerLogAop {

    @AfterThrowing(pointcut = "this(Crawler) && execution(* crawling(String))", throwing = "ex")
    fun loggingException(joinPoint: JoinPoint, ex: Exception) {
        log.warn { "크롤링 중 예외가 발생했습니다. message: ${ex.message} class: ${joinPoint.target}, url: ${joinPoint.args[0]}" }
    }
}
