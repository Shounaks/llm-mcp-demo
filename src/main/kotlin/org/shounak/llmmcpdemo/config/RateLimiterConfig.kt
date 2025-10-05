package org.shounak.llmmcpdemo.config

import io.github.bucket4j.Bandwidth
import io.github.bucket4j.Bucket
import io.github.bucket4j.Refill
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.time.Duration

@Configuration
class RateLimiterConfig {

    // single shared bucket for outbound OpenWeather requests (example: 5 reqs/min)
    @Bean
    fun openWeatherBucket(): Bucket {
        val limit = Bandwidth.classic(5, Refill.intervally(60, Duration.ofMinutes(1)))
        return Bucket.builder().addLimit(limit).build()
    }
}
