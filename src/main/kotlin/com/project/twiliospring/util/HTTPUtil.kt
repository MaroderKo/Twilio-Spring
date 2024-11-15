package com.project.twiliospring.util

import com.project.twiliospring.domain.dto.UserTokensDTO
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletResponse

class HTTPUtil {
    companion object {
        fun createSecuredCookie(name: String, value: String): Cookie {
            return Cookie(name, value).apply {
                isHttpOnly = true
                maxAge = -1
                path = "/"
                setAttribute("SameSite", "Strict")
            }
        }

        fun suspendCookie(name: String, isSecured: Boolean = true): Cookie {
            return Cookie(name, "null").apply {
                path = "/"
                isHttpOnly = isSecured
                maxAge = 0
                setAttribute("SameSite", "Strict")
            }
        }

        fun createPublicCookie(name: String, value: String): Cookie {
            return Cookie(name, value).apply {
                maxAge = -1
                path = "/"
                setAttribute("SameSite", "Strict")
            }
        }

        fun applyNewTokens(response: HttpServletResponse, userTokens: UserTokensDTO) {
            response.addCookie(createSecuredCookie("token", userTokens.accessToken))
            response.addCookie(createSecuredCookie("refresh-token", userTokens.refreshToken))
        }

    }
}