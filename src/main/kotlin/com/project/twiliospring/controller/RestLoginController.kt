package com.project.twiliospring.controller

import com.project.twiliospring.domain.LoginResult
import com.project.twiliospring.domain.User
import com.project.twiliospring.domain.dto.ErrorMessageDTO
import com.project.twiliospring.domain.dto.F2aCodeDTO
import com.project.twiliospring.domain.dto.LoginDTO
import com.project.twiliospring.exception.LoginException
import com.project.twiliospring.exception.SessionNotFoundException
import com.project.twiliospring.service.LoginService
import com.project.twiliospring.util.HTTPUtil
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

private const val SESSION_COOKIE_NAME = "X-SessionId"

@RestController
@RequestMapping("/login")
class RestLoginController(
    val loginService: LoginService
) {
    @PostMapping
    fun login(
        @RequestBody loginDTO: LoginDTO,
        response: HttpServletResponse
    ): ResponseEntity<Unit> {
        val loginResult = loginService.loginByUsernameAndPassword(response, loginDTO.username, loginDTO.password)
        return when (loginResult)
        {
            is LoginResult.Authenticated -> {
                HTTPUtil.applyNewTokens(response, loginResult.tokens)
                response.addCookie(HTTPUtil.createPublicCookie("Authenticated", true.toString()))
                ResponseEntity.ok().build()
            }
            is LoginResult.F2ACodePending -> {
                response.addCookie(HTTPUtil.createPublicCookie(SESSION_COOKIE_NAME, loginResult.sessionId))
                ResponseEntity.status(HttpStatus.ACCEPTED).build()
            }
        }
    }

    @PostMapping("/code")
    fun confirmCode(
        request: HttpServletRequest,
        @RequestBody f2aCodeDTO: F2aCodeDTO,
        response: HttpServletResponse
    ): ResponseEntity<Unit> {
        val sessionId =
            request.cookies.find { it.name == SESSION_COOKIE_NAME }?.value ?: throw SessionNotFoundException()
        val tokens = loginService.loginF2A(response, sessionId, f2aCodeDTO.code)
        response.addCookie(HTTPUtil.createPublicCookie("Authenticated", true.toString()))
        response.addCookie(HTTPUtil.suspendCookie(SESSION_COOKIE_NAME, false))
        HTTPUtil.applyNewTokens(response, tokens)
        return ResponseEntity.ok().build()

    }

    @PostMapping("/register")
    fun register(@RequestBody user: User) {
        loginService.registerUser(user)
    }

    @GetMapping("/ping")
    fun ping(): ResponseEntity<Unit> {
        return ResponseEntity.status(HttpStatus.OK).build()
    }

    @PostMapping("/logout")
    fun logout(response: HttpServletResponse): ResponseEntity<Unit> {
        response.reset()
        response.addCookie(HTTPUtil.createPublicCookie("Authenticated", "False"))
        response.addCookie(HTTPUtil.suspendCookie("token"))
        response.addCookie(HTTPUtil.suspendCookie("refresh-token"))
        return ResponseEntity.ok().build()
    }

    @ExceptionHandler(LoginException::class)
    fun f2ARecordExpiredExceptionHandler(exception: LoginException): ResponseEntity<ErrorMessageDTO> {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorMessageDTO(exception))
    }
}