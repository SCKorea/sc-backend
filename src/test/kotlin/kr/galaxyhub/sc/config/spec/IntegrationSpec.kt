package kr.galaxyhub.sc.config.spec

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.extensions.spring.SpringExtension
import kr.galaxyhub.sc.config.EnableTestcontianers
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
@EnableTestcontianers
abstract class IntegrationSpec(body: DescribeSpec.() -> Unit = {}) : DescribeSpec(body) {

    override fun extensions() = listOf(SpringExtension)
}
