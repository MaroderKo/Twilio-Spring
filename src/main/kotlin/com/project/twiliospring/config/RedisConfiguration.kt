package com.project.twiliospring.config

import io.lettuce.core.RedisClient
import io.lettuce.core.RedisURI
import org.springframework.boot.autoconfigure.data.redis.RedisProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.core.RedisKeyValueAdapter
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories

@Configuration
@EnableRedisRepositories(enableKeyspaceEvents = RedisKeyValueAdapter.EnableKeyspaceEvents.ON_STARTUP)
class RedisConfiguration(
    val redisProperties: RedisProperties
) {


    @Bean
    fun redisClient(): RedisClient {
        return RedisClient.create(RedisURI.create(redisProperties.host, redisProperties.port))
    }

}