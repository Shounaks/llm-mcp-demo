package org.shounak.llmmcpdemo

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cache.annotation.EnableCaching

@SpringBootApplication
@EnableCaching
class LlmMcpDemoApplication

fun main(args: Array<String>) {
    runApplication<LlmMcpDemoApplication>(*args)
}
