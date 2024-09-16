package com.project.twiliospring.repository

import com.project.twiliospring.domain.F2ARecord
import com.project.twiliospring.domain.User

interface F2ARepository {
    fun getRecord(user: User): F2ARecord?
    fun setRecord(user: User, record: F2ARecord)
    fun deleteRecord(user: User)
}