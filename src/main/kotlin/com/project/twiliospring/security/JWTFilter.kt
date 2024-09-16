package com.project.twiliospring.security

import com.project.twiliospring.domain.User
import com.project.twiliospring.service.UserService
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JWTFilter (
    val userService: UserService,
    val jwtProvider: JWTProvider
) : OncePerRequestFilter()
{
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val token = request.getHeader("Authorization")
        val user = token?.let { jwtProvider.getUser(it) }
        if (user != null) {
            val context = SecurityContextHolder.getContext()
            context.authentication = UsernamePasswordAuthenticationToken(user.id,user.copy(password = ""))
        }

        filterChain.doFilter(request, response)
    }

}
