package com.project.twiliospring.repository.impl

import com.project.twiliospring.domain.F2ARecord
import com.project.twiliospring.repository.F2ARepository
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class F2ARepositoryImpl : F2ARepository {
    val storage = LinkedHashMap<String, F2ARecord>()
    override fun getRecord(sessionId: String): F2ARecord? {
        return storage[sessionId]
    }

    override fun setRecord(sessionId: String, record: F2ARecord) {
        storage[sessionId] = record
    }

    override fun deleteRecord(sessionId: String) {
        storage.remove(sessionId)
    }

    override fun clearOldRecords() {
        storage.entries.removeAll { entry -> entry.value.expirationDate.isBefore(LocalDateTime.now()) }
    }


}