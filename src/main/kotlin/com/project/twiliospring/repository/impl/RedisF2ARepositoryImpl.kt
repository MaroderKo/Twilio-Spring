package com.project.twiliospring.repository.impl

import com.fasterxml.jackson.databind.ObjectMapper
import com.project.twiliospring.domain.F2ARecord
import com.project.twiliospring.repository.F2ARepository
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import org.springframework.stereotype.Component

@Component
class RedisF2ARepositoryImpl(
    private val lettuceConnectionFactory: LettuceConnectionFactory,
    private val objectMapper: ObjectMapper
) : F2ARepository {
    override fun getRecord(sessionId: String): F2ARecord? {
        lettuceConnectionFactory.connection.use { connection ->
            val bytes = connection.stringCommands()[sessionId.toByteArray()]
            return bytes?.let { objectMapper.readerFor(F2ARecord::class.java).readValue(it) }!!
        }

    }

    override fun setRecord(sessionId: String, record: F2ARecord) {
        lettuceConnectionFactory.connection.use { connection ->
            connection.stringCommands()
                .setEx(sessionId.toByteArray(), 600, objectMapper.writeValueAsBytes(record))
        }
    }

    override fun deleteRecord(sessionId: String) {
        lettuceConnectionFactory.connection
            .use { connection -> connection.commands().expireAt(sessionId.toByteArray(), 1) }
    }


}