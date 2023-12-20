package kr.galaxyhub.sc.common.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.client.WebClient

/**
 * 공통 설정을 해놓은 WebClient를 빈으로 등록하는 클래스
 */
@Configuration
class WebClientConfig {

    @Bean
    fun webClient(): WebClient {
        return WebClient.builder()
            .build()
    }
}
