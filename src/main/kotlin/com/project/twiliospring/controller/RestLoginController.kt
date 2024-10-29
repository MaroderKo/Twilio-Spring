package com.project.twiliospring.controller

import com.project.twiliospring.domain.LoginResult
import com.project.twiliospring.domain.User
import com.project.twiliospring.domain.dto.F2aCodeDTO
import com.project.twiliospring.domain.dto.LoginDTO
import com.project.twiliospring.exception.F2ARecordExpiredException
import com.project.twiliospring.service.LoginService
import com.project.twiliospring.util.HTTPUtil
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/login")
class RestLoginController(
    val loginService: LoginService
) {
    @PostMapping
    fun login(
        @RequestBody loginDTO: LoginDTO,
        response: HttpServletResponse
    ): ResponseEntity<Any> {
        val loginResult = loginService.loginByUsernameAndPassword(response, loginDTO.username, loginDTO.password)
        return when (loginResult)
        {
            is LoginResult.NotAuthenticated -> {
                ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username or password is incorrect")
            }

            is LoginResult.Authenticated -> {
                HTTPUtil.applyNewTokens(response, loginResult.tokens)
                response.addCookie(HTTPUtil.createPublicCookie("Authenticated", true.toString()))
                ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Username or password is incorrect")
            }
            is LoginResult.F2ACodePending -> {
                ResponseEntity.status(HttpStatus.ACCEPTED).body(loginResult.sessionId)
            }
        }
    }

    @PostMapping("/code")
    fun confirmCode(
        request: HttpServletRequest,
        @RequestBody f2aCodeDTO: F2aCodeDTO,
        response: HttpServletResponse
    ): ResponseEntity<Any> {
        val sessionId = request.getHeader("X-SessionId")!!
        val loginResult = loginService.loginF2A(response, sessionId, f2aCodeDTO.code)
        return when (loginResult)
        {
            is LoginResult.Authenticated -> {
                response.addCookie(HTTPUtil.createPublicCookie("Authenticated", true.toString()))
                HTTPUtil.applyNewTokens(response, loginResult.tokens)
                ResponseEntity.ok().build()
            }

            is LoginResult.NotAuthenticated -> {
                ResponseEntity.status(HttpStatus.BAD_REQUEST).body("2FA code is incorrect")
            }

            else -> {
                ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()
            }
        }
    }

    @PostMapping("/register")
    fun register(@RequestBody user: User) {
        println(user)
        loginService.registerUser(user)
    }

    @GetMapping("/ping")
    fun ping(): ResponseEntity<Any> {
        return ResponseEntity.status(HttpStatus.OK).build()
    }

    @PostMapping("/logout")
    fun logout(response: HttpServletResponse): ResponseEntity<Any> {
        response.reset()
        response.addCookie(HTTPUtil.createPublicCookie("Authenticated", "False"))
        response.addCookie(HTTPUtil.suspendCookie("token"))
        response.addCookie(HTTPUtil.suspendCookie("refresh-token"))
        return ResponseEntity.ok().build()
    }

    @ExceptionHandler(F2ARecordExpiredException::class)
    fun f2ARecordExpiredExceptionHandler(exception: F2ARecordExpiredException): ResponseEntity<Any> {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("F2A record expired")
    }
}