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
        logger.debug("Request to ${request.method} ${request.requestURI}{}")

        val (token, authenticated) = checkTokens(request, response)

        if (authenticated) {
            authenticateUserByToken(token!!)
            response.addCookie(HTTPUtil.createPublicCookie("Authenticated", "True"))
        } else {
            response.addCookie(HTTPUtil.createPublicCookie("Authenticated", "False"))
        }

        filterChain.doFilter(request, response)
    }

    private fun checkTokens(
        request: HttpServletRequest,
        response: HttpServletResponse
    ): Pair<String?, Boolean> {
        var token = request.cookies?.firstOrNull { it.name == "token" }?.value
        val refreshToken = request.cookies?.firstOrNull { it.name == "refresh-token" }?.value

        if (!isTokenValid(token)) {
            // Перевірка refresh-токену, якщо основний токен недійсний
            if (isTokenValid(refreshToken)) {
                try {
                    val newTokens: UserTokensDTO = refreshTokens(refreshToken!!)
                    HTTPUtil.applyNewTokens(response, newTokens)
                    token = newTokens.accessToken
                    return Pair(token, true)
                } catch (_: UserNotFoundException) {
                    clearAuthenticationData(response)
                    return Pair(null, false)
                }
            } else {
                clearAuthenticationData(response)
                return Pair(null, false)
            }
        } else {
            return Pair(token, true)
        }
    }

    private fun authenticateUserByToken(token: String) {
        val user = token.let { jwtProvider.getUser(it) }
        if (user != null) {
            val context = SecurityContextHolder.getContext()
            val authenticationToken =
                UsernamePasswordAuthenticationToken.authenticated(user.id, user, mutableListOf())
            context.authentication = authenticationToken
        }
    }

    private fun isTokenValid(token: String?): Boolean {
        return token != null && jwtProvider.isTokenValid(token)
    }

    private fun refreshTokens(refreshToken: String): UserTokensDTO {
        val user = jwtProvider.getUser(refreshToken)?.copy(password = "") ?: throw UserNotFoundException()
        return jwtProvider.generateTokens(user)
    }

    private fun clearAuthenticationData(response: HttpServletResponse) {
        response.addCookie(HTTPUtil.createPublicCookie("Authenticated", "False"))
        response.addCookie(HTTPUtil.suspendCookie("token"))
        response.addCookie(HTTPUtil.suspendCookie("refresh-token"))
        response.addCookie(HTTPUtil.suspendCookie("role", false))
    }

}
