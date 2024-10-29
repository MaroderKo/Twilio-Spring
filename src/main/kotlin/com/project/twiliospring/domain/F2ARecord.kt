package com.project.twiliospring.domain

import com.project.twiliospring.exception.F2ARecordExpiredException
import java.time.LocalDateTime

data class F2ARecord(
    val code: String,
    val expirationDate: LocalDateTime = LocalDateTime.now().plusMinutes(15),
    var wrongCodeCount: Int = 0
) {
    fun checkCode(code: String): Boolean {
        if (expirationDate.isBefore(LocalDateTime.now())) {
            throw F2ARecordExpiredException()
        }
        return if (this.code == code) {
            true
        } else {
            wrongCodeCount += 1
            false
        }
    }
}