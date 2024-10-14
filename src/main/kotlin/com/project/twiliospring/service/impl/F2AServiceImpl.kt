package com.project.twiliospring.service.impl

import com.project.twiliospring.domain.F2ARecord
import com.project.twiliospring.domain.User
import com.project.twiliospring.exception.F2ARecordNotFoundException
import com.project.twiliospring.repository.F2ARepository
import com.project.twiliospring.service.F2AService
import org.springframework.stereotype.Service
import kotlin.random.Random

@Service
class F2AServiceImpl(
    val repository: F2ARepository
) : F2AService {
    override fun createRecord(sessionId: String, user: User): String {
        val f2aCode = Random.nextInt(100000, 1000000).toString()
        repository.setRecord(
            sessionId,
            F2ARecord(user = user, code = f2aCode)
        )
        return f2aCode
    }

    override fun checkCode(sessionId: String, code: String): User? {
        val record = repository.getRecord(sessionId)
        val isCodeRight = record?.checkCode(code) ?: throw F2ARecordNotFoundException()
        if (!isCodeRight) {
            record.wrongCodeCount += 1
            if (record.wrongCodeCount >= 3) {
                repository.deleteRecord(sessionId)
            }
        } else {
            repository.deleteRecord(sessionId)
        }
        return record.user
    }
}