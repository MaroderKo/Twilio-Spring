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
            }
        }

        fun suspendCookie(name: String, secured: Boolean = true): Cookie {
            return Cookie(name, "null").apply {
                path = "/"
                isHttpOnly = secured
                maxAge = 0
            }
        }

        fun createPublicCookie(name: String, value: String): Cookie {
            return Cookie(name, value).apply {
                maxAge = -1
                path = "/"
            }
        }

        fun applyNewTokens(response: HttpServletResponse, userTokens: UserTokensDTO) {
            response.addCookie(createSecuredCookie("token", userTokens.accessToken))
            response.addCookie(createSecuredCookie("refresh-token", userTokens.refreshToken))
        }

    }
}