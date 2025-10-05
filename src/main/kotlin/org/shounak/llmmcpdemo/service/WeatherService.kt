package org.shounak.llmmcpdemo.service

import io.github.bucket4j.Bucket
import io.micrometer.core.instrument.MeterRegistry
import kotlinx.coroutines.reactor.awaitSingle
import org.shounak.llmmcpdemo.data.OpenWeatherResponse
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Service
class WeatherService(
    private val webClient: WebClient,
    @Value("\${openweather.api.key}") private val apiKey: String,
    @Value("\${openweather.api.base-url}") private val baseUrl: String,
    private val bucket: Bucket,
    private val meterRegistry: MeterRegistry
) {
    private val log = LoggerFactory.getLogger(WeatherService::class.java)

    // caching on city
    @Cacheable("weather")
    suspend fun getCurrentWeatherForCity(city: String): OpenWeatherResponse {
        // rate limit check
        if (!bucket.tryConsume(1)) {
            meterRegistry.counter("weather.rate_limited").increment()
            throw RateLimitException("Rate limit exceeded for OpenWeather calls")
        }

        meterRegistry.counter("weather.requests").increment()
        val encoded = URLEncoder.encode(city, StandardCharsets.UTF_8)
        val uri = "$baseUrl/weather?q=$encoded&units=metric&appid=$apiKey"

        try {
            val start = System.nanoTime()
            val respMono: Mono<OpenWeatherResponse> = webClient.get()
                .uri(uri)
                .retrieve()
                .bodyToMono(OpenWeatherResponse::class.java)
            val resp = respMono.awaitSingle()
            val elapsed = (System.nanoTime() - start) / 1_000_000
            meterRegistry.timer("weather.latency").record(java.time.Duration.ofMillis(elapsed))
            return resp
        } catch (ex: Exception) {
            log.error("OpenWeather fetch failed for city=$city", ex)
            meterRegistry.counter("weather.errors").increment()
            throw ExternalServiceException("Failed to fetch weather for $city: ${ex.message}")
        }
    }
}

// EXCEPTION CLASSES - For Quick poc building (ik this should be its own files)
class RateLimitException(message: String) : RuntimeException(message)
class ExternalServiceException(message: String) : RuntimeException(message)
