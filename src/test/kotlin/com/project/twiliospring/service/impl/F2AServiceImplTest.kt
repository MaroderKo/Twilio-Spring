package com.project.twiliospring.service.impl

import com.project.twiliospring.domain.F2ARecord
import com.project.twiliospring.exception.F2ARecordNotFoundException
import com.project.twiliospring.repository.F2ARepository
import com.project.twiliospring.service.MessengerService
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.anyString
import org.mockito.Mockito.mock
import org.mockito.Mockito.startsWith
import org.mockito.kotlin.*
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class F2AServiceImplTest {

    private val repository = mock(F2ARepository::class.java)
    private val messengerService = mock(MessengerService::class.java)
    private val service = F2AServiceImpl(repository, messengerService)

    private val number = "1234567890"
    private val code = "123456"

    @Test
    fun `createRecord generates and stores code, then sends message`() {
        service.createRecord(number)

        verify(repository).setRecord(eq(number), any())
        verify(messengerService).sendMessage(startsWith("2FA login code:"), eq(number))
    }

    @Test
    fun `isCodeValid returns true and deletes record if code is valid`() {
        val record = mock(F2ARecord::class.java)

        whenever(repository.getRecord(number)).thenReturn(record)
        whenever(record.isCodeValid(code)).thenReturn(true)

        assertTrue(service.isCodeValid(number, code))
        verify(repository).deleteRecord(number)
    }

    @Test
    fun `isCodeValid returns false if code is invalid`() {
        val record = mock(F2ARecord::class.java)

        whenever(repository.getRecord(number)).thenReturn(record)
        whenever(record.isCodeValid(code)).thenReturn(false)

        assertFalse(service.isCodeValid(number, code))
        verify(repository, never()).deleteRecord(anyString())
    }

    @Test
    fun `isCodeValid throws exception if record not found`() {
        whenever(repository.getRecord(number)).thenReturn(null)

        assertThrows<F2ARecordNotFoundException> {
            service.isCodeValid(number, code)
        }
    }


}