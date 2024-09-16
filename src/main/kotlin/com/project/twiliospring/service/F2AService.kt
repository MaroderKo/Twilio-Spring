package com.project.twiliospring.service

import com.project.twiliospring.domain.F2ARecord
import com.project.twiliospring.domain.User

interface F2AService {
    fun createRecord(user: User)
    fun checkCode(user: User, code: String): Boolean
}