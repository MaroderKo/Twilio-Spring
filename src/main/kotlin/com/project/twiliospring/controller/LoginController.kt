package com.project.twiliospring.controller

import com.project.twiliospring.security.JWTProvider
import com.project.twiliospring.service.F2AService
import com.project.twiliospring.service.UserService
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/login")
class LoginController(
    val userService: UserService, val f2aService: F2AService, val jwtProvider: JWTProvider
) {
    @PostMapping
    fun login(
        @RequestParam(value = "username", required = true) username: String,
        @RequestParam(value = "password", required = true) password: String,
        @RequestParam(value = "confirm-code", required = false) confirmCode: String?,
        response: HttpServletResponse
    ): ResponseEntity<Any> {
        val user =
            userService.findByUsernameAndPassword(username, password) ?: return ResponseEntity.badRequest().body("Username or password is incorrect")
        if (confirmCode != null) {
            val isCodeValid = f2aService.checkCode(user, confirmCode.trim())
            if (isCodeValid) {
                val userTokensDTO = jwtProvider.generateTokens(user.copy(password = ""))
                response.addCookie(Cookie("token", userTokensDTO.accessToken).apply {
                        isHttpOnly = true
                        secure = true
                    })
                response.addCookie(Cookie("refresh-token", userTokensDTO.refreshToken).apply {
                        isHttpOnly = true
                        secure = true
                    })

                return ResponseEntity.ok().build()
            }
            else
            {
                return ResponseEntity.badRequest().body("2FA code is incorrect")
            }
        }
        else
        {
            return ResponseEntity.status(HttpStatus.CONTINUE).build()
        }
    }
}