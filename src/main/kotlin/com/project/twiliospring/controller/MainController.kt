package com.project.twiliospring.controller

import com.project.twiliospring.domain.User
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class MainController {
    @GetMapping
    fun placeholder(@AuthenticationPrincipal user: User): String {
        return "Hello ${user.username}!" +
                "<br><a href=\"/message\">Message</a>"
    }
}