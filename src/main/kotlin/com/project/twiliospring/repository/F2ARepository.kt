package com.project.twiliospring.repository

import com.project.twiliospring.domain.F2ARecord

interface F2ARepository {
    fun getRecord(sessionId: String): F2ARecord?
    fun setRecord(sessionId: String, record: F2ARecord)
    fun deleteRecord(sessionId: String)
}