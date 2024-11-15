package com.project.twiliospring.service.impl

import com.project.twiliospring.domain.LoginResult
import com.project.twiliospring.domain.User
import com.project.twiliospring.domain.dto.UserTokensDTO
import com.project.twiliospring.exception.BadCredentialsException
import com.project.twiliospring.exception.InvalidF2ACodeException
import com.project.twiliospring.security.JWTProvider
import com.project.twiliospring.service.F2AService
import com.project.twiliospring.service.LoginService
import com.project.twiliospring.service.UserService
import jakarta.servlet.http.HttpServletResponse
import org.springframework.stereotype.Service
import java.util.*

@Service
class LoginServiceImpl(
    var jwtProvider: JWTProvider,
    var userService: UserService,
    var f2aService: F2AService,
) : LoginService {
    override fun loginByUsernameAndPassword(
        response: HttpServletResponse,
        username: String,
        password: String
    ): LoginResult {
        val user =
            userService.findByUsernameAndPassword(username, password) ?: throw BadCredentialsException()
        if (user.phoneNumber.isNullOrEmpty()) {
            val userTokens = jwtProvider.generateTokens(user)
            return LoginResult.Authenticated(userTokens)
        }
        val sessionId = user.id.toString()
        f2aService.createRecord(user.phoneNumber)
        return LoginResult.F2ACodePending(encodeSessionId(sessionId))
    }

    override fun loginF2A(response: HttpServletResponse, sessionId: String, code: String): UserTokensDTO {
        val decodedSessionId = decodeSessionId(sessionId)
        val user = userService.findById(decodedSessionId)
        val isCodeValid = user?.phoneNumber?.let { f2aService.isCodeValid(it, code.trim()) }
        return if (isCodeValid == true) {
            jwtProvider.generateTokens(user)
        } else {
            throw InvalidF2ACodeException()
        }

    }

    override fun registerUser(user: User): User {
        val registeredUser = userService.save(user)
        return registeredUser
    }

    private fun encodeSessionId(sessionId: String): String {
        return Base64.getEncoder().encodeToString(sessionId.toByteArray())
    }

    private fun decodeSessionId(sessionId: String): Long {
        return String(Base64.getDecoder().decode(sessionId)).toLong()
    }


}