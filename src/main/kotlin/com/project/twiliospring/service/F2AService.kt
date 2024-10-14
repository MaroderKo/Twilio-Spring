package com.project.twiliospring.service

import com.project.twiliospring.domain.User

interface F2AService {
    fun createRecord(sessionId: String, user: User): String
    fun checkCode(sessionId: String, code: String): User?
}