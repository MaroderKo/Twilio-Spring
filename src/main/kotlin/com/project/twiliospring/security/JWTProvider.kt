package com.project.twiliospring.security

import com.project.twiliospring.domain.User
import com.project.twiliospring.domain.dto.UserTokensDTO
import com.project.twiliospring.service.UserService
import io.jsonwebtoken.Claims
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.JwtParser
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.util.*
import java.util.concurrent.TimeUnit

@Component
class JWTProvider(
    val userService: UserService,
    @Value("\${jwt.secret}") val secretKey: String,
    @Value("\${jwt.expiration-time-in-minutes.access}") val accessTokenExpirationTime: Long,
    @Value("\${jwt.expiration-time-in-minutes.refresh}") val refreshTokenExpirationTime: Long,
    @Value("\${jwt.prefix}") val tokenPrefix: String? = null
) {

    private var parser: JwtParser = Jwts.parser().verifyWith(Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey))).build()

    fun generateTokens(user: User): UserTokensDTO {
        val clearUser = user.copy(password = "")
        return UserTokensDTO(
            accessToken = (generateToken(clearUser, false)),
            refreshToken = (generateToken(clearUser, true))
        )
    }

    private fun generateToken(user: User, isRefreshToken: Boolean): String {
        return Jwts.builder()
            .claim("id", user.id)
            .claims()
            .subject(user.username)
            .and()
            .issuedAt(Date(System.currentTimeMillis()))
            .expiration(getExpirationDate(isRefreshToken))
            .signWith(Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey)))
            .compact()
    }

    private fun getExpirationDate(isRefreshToken: Boolean): Date {
        return if (isRefreshToken) {
            Date(Date().time + TimeUnit.MINUTES.toMillis(refreshTokenExpirationTime))
        } else {
            Date(Date().time + TimeUnit.MINUTES.toMillis(accessTokenExpirationTime))
        }
    }

    fun isTokenValid(token: String): Boolean {
        try {
            val claims = extractAllClaims(token)
            return claims.expiration.after(Date(System.currentTimeMillis()))
        } catch (e: ExpiredJwtException) {
            return false
        }
    }

    fun getUser(token: String): User? {
        val userIdClaim = extractAllClaims(token)["id"]
        val userId = if (userIdClaim is Long) userIdClaim else (userIdClaim as Int).toLong()
        return userService.findById(userId)
    }

    private fun extractAllClaims(token: String): Claims {
        return parser.parseSignedClaims(token.replace(tokenPrefix!!, "")).payload
    }
}