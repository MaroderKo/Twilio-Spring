package com.project.twiliospring.domain

import com.project.twiliospring.domain.dto.UserTokensDTO

sealed class LoginResult {
    data class Authenticated(val tokens: UserTokensDTO) : LoginResult()
    data class F2ACodePending(val sessionId: String) : LoginResult()
    data object NotAuthenticated : LoginResult()
}