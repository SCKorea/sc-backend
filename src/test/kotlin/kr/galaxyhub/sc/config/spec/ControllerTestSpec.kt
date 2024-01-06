package kr.galaxyhub.sc.config.spec

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.core.test.TestCase
import io.kotest.core.test.TestResult
import io.mockk.clearAllMocks
import io.mockk.mockkClass
import org.junit.platform.commons.util.ClassFilter
import org.junit.platform.commons.util.ReflectionUtils
import org.springframework.beans.factory.config.BeanFactoryPostProcessor
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.Import
import org.springframework.stereotype.Service

@Import(MockServiceBeanFactoryPostProcessor::class)
@WebMvcTest
@AutoConfigureRestDocs
abstract class ControllerTestSpec(body: DescribeSpec.() -> Unit = {}) : DescribeSpec(body) {

    override suspend fun afterEach(testCase: TestCase, result: TestResult) {
        clearAllMocks()
    }
}

class MockServiceBeanFactoryPostProcessor : BeanFactoryPostProcessor {

    override fun postProcessBeanFactory(beanFactory: ConfigurableListableBeanFactory) {
        val classFilter = ClassFilter.of { it.isAnnotationPresent(Service::class.java) }
        ReflectionUtils.findAllClassesInPackage("kr.galaxyhub.sc", classFilter).forEach {
            beanFactory.registerSingleton(it.simpleName, mockkClass(it.kotlin))
        }
    }
}
