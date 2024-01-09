package kr.galaxyhub.sc.config.spec

import io.kotest.core.spec.style.DescribeSpec
import kr.galaxyhub.sc.config.EnableTestcontianers
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
@EnableTestcontianers
abstract class IntegrationSpec(body: DescribeSpec.() -> Unit = {}) : DescribeSpec(body)
