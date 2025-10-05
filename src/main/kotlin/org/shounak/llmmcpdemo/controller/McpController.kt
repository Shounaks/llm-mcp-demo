package org.shounak.llmmcpdemo.controller

import io.micrometer.observation.annotation.Observed
import org.shounak.llmmcpdemo.service.LLMService
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/mcp")
class MCPController(
    private val llmService: LLMService
) {
    private val logger = LoggerFactory.getLogger(MCPController::class.java)

    @GetMapping("/hello")
    suspend fun hello():ResponseEntity<String> = ResponseEntity.ok("Hello World")

    /**
     * Endpoint for MCP-style weather query
     * Example: GET /mcp/weather?city=Paris
     */
    @GetMapping("/weather")
    @Observed(name = "mcp.weather.request", contextualName = "mcp-weather")
    suspend fun getWeatherInfo(
        @RequestParam city: String
    ): ResponseEntity<Map<String, Any>> {
        logger.info("Received MCP weather request for city: $city")

        return try {
            val llmResponse = llmService.askLLMForWeather(city)

            val response = mapOf(
                "city" to city,
                "source" to "LLMService",
                "data" to llmResponse,
                "timestamp" to System.currentTimeMillis()
            )

            ResponseEntity.ok(response)
        } catch (ex: Exception) {
            logger.error("Error handling MCP weather request: ${ex.message}", ex)
            ResponseEntity.internalServerError().body(
                mapOf(
                    "error" to "Failed to retrieve weather info",
                    "details" to ex.localizedMessage
                )
            )
        }
    }

    /**
     * Simple health check endpoint for MCP agent verification
     */
    @GetMapping("/health")
    fun healthCheck(): ResponseEntity<Map<String, Any>> {
        return ResponseEntity.ok(
            mapOf(
                "status" to "UP",
                "service" to "MCP Weather Demo",
                "timestamp" to System.currentTimeMillis()
            )
        )
    }
}
