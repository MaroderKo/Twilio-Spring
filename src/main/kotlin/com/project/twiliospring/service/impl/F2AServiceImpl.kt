package com.project.twiliospring.service.impl

import com.project.twiliospring.domain.F2ARecord
import com.project.twiliospring.exception.F2ARecordNotFoundException
import com.project.twiliospring.repository.F2ARepository
import com.project.twiliospring.service.F2AService
import com.project.twiliospring.service.MessengerService
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service
import kotlin.random.Random

@Service
@Profile("!twilio")
class F2AServiceImpl(
    val repository: F2ARepository,
    val messengerService: MessengerService
) : F2AService {
    override fun createRecord(number: String) {
        val f2aCode = Random.nextInt(100000, 1000000).toString()
        repository.setRecord(
            number,
            F2ARecord( code = f2aCode)
        )
        messengerService.sendMessage("2FA login code: $f2aCode", number)
    }

    override fun isCodeValid(number: String, code: String): Boolean {
        val record = repository.getRecord(number)
        val isCodeValid = record?.isCodeValid(code) ?: throw F2ARecordNotFoundException()
        return if (!isCodeValid) {
            false
        } else {
            repository.deleteRecord(number)
            true
        }
    }
}