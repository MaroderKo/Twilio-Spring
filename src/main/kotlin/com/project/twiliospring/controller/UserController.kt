package com.project.twiliospring.controller

import com.project.twiliospring.domain.User
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/user")
class UserController {
    @PostMapping
    fun getUserData(authentication: Authentication): ResponseEntity<Any> {
        val user = authentication.credentials as User
        return ResponseEntity.ok().body(user.copy(password = ""))
    }
}
