package com.project.twiliospring.service

import com.project.twiliospring.domain.LoginState
import com.project.twiliospring.domain.User
import jakarta.servlet.http.HttpServletResponse

interface LoginService {
    fun loginByUsernameAndPassword(response: HttpServletResponse, username: String, password: String): LoginState
    fun loginF2A(response: HttpServletResponse, sessionId: String, code: String): LoginState
    fun registerUser(user: User): User
}