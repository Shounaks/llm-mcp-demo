package org.shounak.llmmcpdemo.config

import com.github.benmanes.caffeine.cache.Caffeine
import org.springframework.cache.CacheManager
import org.springframework.cache.caffeine.CaffeineCacheManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.time.Duration

@Configuration
class CacheConfig {
    @Bean
    fun caffeineConfig(): Caffeine<Any, Any> =
        Caffeine.newBuilder()
            .expireAfterWrite(Duration.ofMinutes(5))
            .maximumSize(10_000)

    @Bean
    fun cacheManager(caffeine: Caffeine<Any, Any>): CacheManager {
        val manager = CaffeineCacheManager("weather")
        manager.setCaffeine(caffeine)
        return manager
    }
}
