package com.project.twiliospring.security

import com.project.twiliospring.domain.dto.UserTokensDTO
import com.project.twiliospring.exception.UserNotFoundException
import com.project.twiliospring.util.HTTPUtil
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JWTFilter(
    val jwtProvider: JWTProvider
) : OncePerRequestFilter()
{
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        println("Request to ${request.method} ${request.requestURI}{}")
        var authenticated = false
        var token = request.cookies?.firstOrNull { cookie -> cookie.name == "token" }?.value
        if (token == null || !jwtProvider.isTokenValid(token)) {
            val refreshToken = request.cookies?.firstOrNull { cookie -> cookie.name == "refresh-token" }?.value
            if (refreshToken != null && jwtProvider.isTokenValid(refreshToken)) {
                try {
                    val newTokens: UserTokensDTO = refreshTokens(refreshToken)
                    HTTPUtil.applyNewTokens(response, newTokens)
                    token = newTokens.accessToken
                    authenticated = true
                } catch (_: UserNotFoundException) {
                }

            }
        } else {
            authenticated = true
        }
        if (authenticated) {
            val user = token?.let { jwtProvider.getUser(it) }
            if (user != null) {
                val context = SecurityContextHolder.getContext()
                val authenticationToken =
                    UsernamePasswordAuthenticationToken.authenticated(user.id, user, mutableListOf())
                context.authentication = authenticationToken
            }
            response.addCookie(HTTPUtil.createPublicCookie("Authenticated", "True"))
        } else {
            response.addCookie(HTTPUtil.createPublicCookie("Authenticated", "False"))
        }
        filterChain.doFilter(request, response)
    }

    private fun refreshTokens(refreshToken: String): UserTokensDTO {
        val user = jwtProvider.getUser(refreshToken)?.copy(password = "") ?: throw UserNotFoundException()
        return jwtProvider.generateTokens(user)
    }

}
