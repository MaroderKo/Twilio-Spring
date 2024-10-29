package com.project.twiliospring.service

import com.project.twiliospring.domain.LoginResult
import com.project.twiliospring.domain.User
import jakarta.servlet.http.HttpServletResponse

interface LoginService {
    fun loginByUsernameAndPassword(response: HttpServletResponse, username: String, password: String): LoginResult
    fun loginF2A(response: HttpServletResponse, sessionId: String, code: String): LoginResult
    fun registerUser(user: User): User
}