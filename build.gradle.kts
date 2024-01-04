import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "3.2.0"
    id("io.spring.dependency-management") version "1.1.4"
    id("org.asciidoctor.jvm.convert") version "3.3.2"
    kotlin("jvm") version "1.9.20"
    kotlin("plugin.spring") version "1.9.20"
    kotlin("plugin.jpa") version "1.9.20"
}

group = "kr.galaxyhub"
version = "0.0.1-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_17
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

val asciidoctorExt = "asciidoctorExt"
configurations.create(asciidoctorExt) {
    extendsFrom(configurations.testImplementation.get())
}

val snippetsDir = file("build/generated-snippets")

val ulidCreatorVersion = "5.2.2"
val kotlinLoggingVersion = "5.1.1"
val jsoupVersion = "1.17.1"
val jjwtVersion = "0.12.3"
val mockkVersion = "1.13.4"
val springMockkVersion = "4.0.2"
val testcontainersVersion = "1.19.3"
val kotestVersion = "5.7.2"
val kotestExtensionTestcontainers = "2.0.2"
val kotestExtensionSpring = "1.1.3"

dependencies {
    // Spring
    implementation("org.springframework.boot:spring-boot-starter-data-jdbc")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-jdbc")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.flywaydb:flyway-core")
    implementation("org.flywaydb:flyway-mysql")
    implementation("org.jetbrains.kotlin:kotlin-reflect")

    // https://mvnrepository.com/artifact/com.github.f4b6a3/ulid-creator
    // ULID Creator
    implementation("com.github.f4b6a3:ulid-creator:$ulidCreatorVersion")

    // Kotlin logging
    implementation("io.github.oshai:kotlin-logging-jvm:$kotlinLoggingVersion")

    // Jsoup
    implementation("org.jsoup:jsoup:$jsoupVersion")

    // JWT
    implementation("io.jsonwebtoken:jjwt-api:$jjwtVersion")

    // DB Connectors
    runtimeOnly("com.h2database:h2")
    runtimeOnly("com.mysql:mysql-connector-j")

    // JWT
    runtimeOnly("io.jsonwebtoken:jjwt-impl:$jjwtVersion")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:$jjwtVersion")

    // Spring
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.restdocs:spring-restdocs-mockmvc")

    // Mockk
    testImplementation("io.mockk:mockk:$mockkVersion")
    testImplementation("io.mockk:mockk-jvm:$mockkVersion")

    // Spring Mockk
    testImplementation("com.ninja-squad:springmockk:$springMockkVersion")

    // Testcontainers
    testImplementation("org.testcontainers:testcontainers:$testcontainersVersion")

    // Kotest
    testImplementation("io.kotest:kotest-runner-junit5:$kotestVersion")
    testImplementation("io.kotest:kotest-assertions-core:$kotestVersion")
    testImplementation("io.kotest.extensions:kotest-extensions-testcontainers:$kotestExtensionTestcontainers")
    testImplementation("io.kotest.extensions:kotest-extensions-spring:$kotestExtensionSpring")

    // Mock Web Server
    testImplementation("com.squareup.okhttp3:mockwebserver")

    asciidoctorExt("org.springframework.restdocs:spring-restdocs-asciidoctor")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs += "-Xjsr305=strict"
        jvmTarget = "17"
    }
}

tasks.test {
    outputs.dir(snippetsDir)
    useJUnitPlatform()
}

tasks.asciidoctor {
    inputs.dir(snippetsDir)
    dependsOn(tasks.test)
    configurations(asciidoctorExt)
    baseDirFollowsSourceFile()
}

val copyDocument = tasks.register<Copy>("copyDocument") {
    description = "생성된 Asciidoc 문서를 resource/static 폴더로 이동"
    group = JavaBasePlugin.DOCUMENTATION_GROUP
    dependsOn(tasks.asciidoctor)
    doFirst {
        delete(file("src/main/resources/static/docs"))
    }
    from(file("build/docs/asciidoc"))
    into(file("src/main/resources/static/docs"))
}

tasks.build {
    dependsOn(copyDocument)
}

tasks.bootJar {
    dependsOn(copyDocument)
}

allOpen {
    annotation("jakarta.persistence.Entity")
    annotation("jakarta.persistence.Embeddable")
    annotation("jakarta.persistence.MappedSuperclass")
}

tasks.jar {
    enabled = false
}
