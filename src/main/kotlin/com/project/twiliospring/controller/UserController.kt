package com.project.twiliospring.controller

import com.project.twiliospring.domain.User
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/user")
class UserController {
    @GetMapping
    fun getUserData(authentication: Authentication): ResponseEntity<User> {
        val user = authentication.credentials as User
        return ResponseEntity.ok().body(user.copy(password = ""))
    }
}
