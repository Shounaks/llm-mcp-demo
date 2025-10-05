package org.shounak.llmmcpdemo.service

import com.github.benmanes.caffeine.cache.Caffeine
import io.github.resilience4j.kotlin.ratelimiter.executeSuspendFunction
import io.github.resilience4j.ratelimiter.RateLimiter
import io.github.resilience4j.ratelimiter.RateLimiterConfig
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBody
import org.slf4j.LoggerFactory
import java.time.Duration

@Service
class LLMService(
    private val webClientBuilder: WebClient.Builder,
    @Value("\${openai.api.key}") private val openaiApiKey: String
) {

    private val logger = LoggerFactory.getLogger(LLMService::class.java)

    // Caching layer using Caffeine
    private val cache = Caffeine.newBuilder()
        .expireAfterWrite(Duration.ofMinutes(10))
        .maximumSize(100)
        .build<String, String>()

    // Mutex to ensure coroutine-safe cache writes
    private val cacheLock = Mutex()

    // Resilience4j RateLimiter
    private val rateLimiterConfig = RateLimiterConfig.custom()
        .limitForPeriod(5) // max 5 calls per second
        .limitRefreshPeriod(Duration.ofSeconds(1))
        .timeoutDuration(Duration.ofSeconds(2))
        .build()

    private val rateLimiter = RateLimiter.of("llmService", rateLimiterConfig)

    private val webClient: WebClient = webClientBuilder
        .baseUrl("https://api.openai.com/v1/chat/completions")
        .defaultHeader("Authorization", "Bearer $openaiApiKey")
        .build()

    suspend fun askLLMForWeather(city: String): String = withContext(Dispatchers.IO) {
        val cacheKey = "weather-$city"
        cache.getIfPresent(cacheKey)?.let {
            logger.info("Cache hit for city: $city")
            return@withContext it
        }

        return@withContext try {
            val response = rateLimiter.executeSuspendFunction {
                val payload = mapOf(
                    "model" to "gpt-4o-mini",
                    "messages" to listOf(
                        mapOf("role" to "system", "content" to "You are a weather assistant."),
                        mapOf("role" to "user", "content" to "Get current weather in $city.")
                    )
                )

                webClient.post()
                    .bodyValue(payload)
                    .retrieve()
                    .awaitBody<Map<String, Any>>()
            }

            val text = ((response["choices"] as? List<*>)?.firstOrNull() as? Map<*, *>)?.get("message")
                ?.let { (it as Map<*, *>)["content"].toString() }
                ?: "Weather info unavailable."

            cacheLock.withLock {
                cache.put(cacheKey, text)
            }

            text
        } catch (ex: Exception) {
            logger.error("LLM call failed: ${ex.message}", ex)
            "Error fetching weather info: ${ex.localizedMessage}"
        }
    }
}
