package com.project.twiliospring.controller

import com.project.twiliospring.domain.LoginState
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
        val loginStatus = loginService.loginByUsernameAndPassword(response, loginDTO.username, loginDTO.password)
        if (loginStatus == LoginState.NOT_AUTHENTICATED) {
            return ResponseEntity.status(loginStatus.getResponseStatus()).body("Username or password is incorrect")
        }
        return ResponseEntity.status(loginStatus.getResponseStatus()).build()
    }

    @PostMapping("/code")
    fun confirmCode(
        request: HttpServletRequest,
        @RequestBody f2aCodeDTO: F2aCodeDTO,
        response: HttpServletResponse
    ): ResponseEntity<Any> {
        val sessionId = request.cookies.find { it.name == "SESSION_ID" }?.value!!
        val loginStatus = loginService.loginF2A(response, sessionId, f2aCodeDTO.code)

        if (loginStatus == LoginState.NOT_AUTHENTICATED) {
            return ResponseEntity.status(loginStatus.getResponseStatus()).body("2FA code is incorrect")
        }
        return ResponseEntity.status(loginStatus.getResponseStatus()).build()
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