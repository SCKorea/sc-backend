package kr.galaxyhub.sc.config

import io.kotest.core.extensions.ProjectExtension
import io.kotest.core.project.ProjectContext
import io.kotest.core.spec.AutoScan
import io.kotest.mpp.hasAnnotation
import java.io.File
import kotlin.reflect.full.superclasses
import org.slf4j.LoggerFactory
import org.testcontainers.containers.DockerComposeContainer
import org.testcontainers.containers.output.Slf4jLogConsumer
import org.testcontainers.containers.wait.strategy.ShellStrategy

@AutoScan
class TestContainerExtension : ProjectExtension {

    private var containers = mutableListOf<DockerComposeContainer<*>>()

    override suspend fun interceptProject(context: ProjectContext, callback: suspend (ProjectContext) -> Unit) {
        if (isTestcontianersEnable(context)) {
            DockerComposeContainer(File("src/test/resources/docker-compose.yml")).apply {
                waitingFor(
                    "test_mysql",
                    ShellStrategy().withCommand("mysql -u'test' -p'1234' -e'select 1' && sleep 2")
                )
                withLogConsumer("test_mysql", Slf4jLogConsumer(LoggerFactory.getLogger(javaClass)))
                start()
            }.also {
                containers.add(it)
            }
        }
        callback(context)
        containers.forEach { it.stop() }
    }

    private fun isTestcontianersEnable(context: ProjectContext): Boolean {
        val hasAnnotationItself = context.suite.specs.stream()
            .anyMatch { it.kclass.hasAnnotation<EnableTestcontianers>() }
        return if (hasAnnotationItself) true else context.suite.specs.stream()
            .flatMap { it.kclass.superclasses.stream() }
            .anyMatch { it.hasAnnotation<EnableTestcontianers>() }
    }
}
