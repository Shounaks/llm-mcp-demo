plugins {
    kotlin("jvm") version "1.9.25"
    kotlin("plugin.spring") version "1.9.25"
    id("org.springframework.boot") version "3.5.6"
    id("io.spring.dependency-management") version "1.1.7"
}

group = "org.shounak"
version = "0.0.1-SNAPSHOT"
description = "llm-mcp-demo"

// CONSTANTS
val bucket4jVersion = "8.3.0"
val resilience4jSpringBootVersion = "2.0.2"
val resilience4jReactorVersion = "2.0.2"
val caffeineCacheVersion = "3.1.8"
java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

dependencies {
    // Dependancies
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.springframework.boot:spring-boot-starter-actuator")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor:1.8.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor:1.8.1")
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")

    // Rate limiter
//    implementation("com.github.vladimir-bukhtoyarov:bucket4j-core:$bucket4jVersion")
    // https://mvnrepository.com/artifact/com.bucket4j/bucket4j_jdk8-core
    implementation("com.bucket4j:bucket4j_jdk8-core:$bucket4jVersion")

    // Resilience4j (retry + circuitbreaker)
    implementation("io.github.resilience4j:resilience4j-spring-boot3:$resilience4jSpringBootVersion")
    implementation("io.github.resilience4j:resilience4j-kotlin:$resilience4jSpringBootVersion")
    implementation("io.github.resilience4j:resilience4j-ratelimiter:$resilience4jSpringBootVersion")

    // Micrometer
    implementation("io.micrometer:micrometer-core")
    implementation("io.micrometer:micrometer-registry-prometheus")

    // Caching
    implementation("com.github.ben-manes.caffeine:caffeine:$caffeineCacheVersion")
    implementation("org.springframework.boot:spring-boot-starter-cache")

    //    Plugins
    compileOnly("org.projectlombok:lombok")
    developmentOnly("org.springframework.boot:spring-boot-devtools")

    //    Annotation Preprocessor
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
    annotationProcessor("org.projectlombok:lombok")

    //    Testing
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
