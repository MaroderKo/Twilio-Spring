package com.project.twiliospring.controller

import com.project.twiliospring.domain.User
import com.project.twiliospring.service.MessengerService
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import java.time.LocalDateTime

@Controller
class TwilioController(
    val twilioService: MessengerService
) {

    @RequestMapping("/message")
    fun call(@AuthenticationPrincipal user: User, response: HttpServletResponse) {
        twilioService.sendMessage(
            LocalDateTime.now().toString(),
            user = user
        )
        response.sendRedirect("/")
    }
}