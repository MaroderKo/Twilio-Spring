package com.project.twiliospring.service.impl

import com.project.twiliospring.domain.LoginState
import com.project.twiliospring.domain.User
import com.project.twiliospring.security.JWTProvider
import com.project.twiliospring.service.F2AService
import com.project.twiliospring.service.LoginService
import com.project.twiliospring.service.MessengerService
import com.project.twiliospring.service.UserService
import com.project.twiliospring.util.HTTPUtil
import jakarta.servlet.http.HttpServletResponse
import org.springframework.stereotype.Service
import java.util.*

@Service
class LoginServiceImpl(
    var jwtProvider: JWTProvider,
    var userService: UserService,
    var f2aService: F2AService,
    var messengerService: MessengerService
) : LoginService {
    override fun loginByUsernameAndPassword(
        response: HttpServletResponse,
        username: String,
        password: String
    ): LoginState {
        val user =
            userService.findByUsernameAndPassword(username, password) ?: return LoginState.NOT_AUTHENTICATED
        if (user.phoneNumber.isNullOrEmpty()) {
            val userTokens = jwtProvider.generateTokens(user)
            HTTPUtil.applyNewTokens(response, userTokens)
            response.addCookie(HTTPUtil.createPublicCookie("Authenticated", "True"))
            return LoginState.AUTHENTICATED
        }
        val sessionId = UUID.randomUUID().toString()
        val code = f2aService.createRecord(sessionId, user)
        messengerService.sendMessage("2FA login code: $code", user)
//        println("\nf2a code: $code\n")
//        println("Session ID: $sessionId")
        response.addCookie(HTTPUtil.createSecuredCookie("SESSION_ID", sessionId))
        response.addCookie(HTTPUtil.createPublicCookie("Authenticated", "False"))
        return LoginState.F2A_CODE_PENDING
    }

    override fun loginF2A(response: HttpServletResponse, sessionId: String, code: String): LoginState {
        val user = f2aService.checkCode(sessionId, code.trim())
        if (user != null) {
            val userTokens = jwtProvider.generateTokens(user)
            HTTPUtil.applyNewTokens(response, userTokens)
            response.addCookie(HTTPUtil.suspendCookie("SESSION_ID"))
            response.addCookie(HTTPUtil.createPublicCookie("Authenticated", "True"))
            return LoginState.AUTHENTICATED
        } else {
            return LoginState.NOT_AUTHENTICATED
        }
    }

    override fun registerUser(user: User): User {
        val registeredUser = userService.save(user)
        return registeredUser
    }
}