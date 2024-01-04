package kr.galaxyhub.sc.config.spec

import io.kotest.core.extensions.install
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.extensions.testcontainers.ContainerLifecycleMode
import io.kotest.extensions.testcontainers.DockerComposeContainerExtension
import java.io.File
import org.slf4j.LoggerFactory
import org.springframework.boot.test.context.SpringBootTest
import org.testcontainers.containers.output.Slf4jLogConsumer
import org.testcontainers.containers.wait.strategy.ShellStrategy

@SpringBootTest
abstract class IntegrationSpec(body: DescribeSpec.() -> Unit = {}) : DescribeSpec(body) {

    private val log = LoggerFactory.getLogger(javaClass)

    init {
        install(DockerComposeContainerExtension(File("src/test/resources/docker-compose.yml"), ContainerLifecycleMode.Project))
            .apply {
                waitingFor("test_mysql", ShellStrategy().withCommand("mysql -u'test' -p'1234' -e'select 1' && sleep 2"))
                withLogConsumer("test_mysql", Slf4jLogConsumer(log))
            }.apply {
                start()
            }
    }
}
