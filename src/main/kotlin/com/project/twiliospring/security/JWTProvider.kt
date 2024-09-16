package com.project.twiliospring.security

import com.project.twiliospring.domain.User
import com.project.twiliospring.domain.dto.UserTokensDTO
import com.project.twiliospring.service.UserService
import io.jsonwebtoken.Claims
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
    @Value("\${jwt.expiration-time-in-minutes.access}") val accessTokenExpirationTime: Long? = null,
    @Value("\${jwt.expiration-time-in-minutes.refresh}") val refreshTokenExpirationTime: Long? = null,
    @Value("\${jwt.prefix}") val tokenPrefix: String? = null
) {

    private var parser: JwtParser = Jwts.parser().verifyWith(Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey))).build()

    fun generateTokens(user: User): UserTokensDTO {
        return UserTokensDTO(
            accessToken = (generateToken(user, false)),
            refreshToken = (generateToken(user, true))
        )
    }

    private fun generateToken(user: User, isRefreshToken: Boolean): String {
        return Jwts.builder()
            .claim("id", user.id)
            .claims()
            .subject(user.username)
            .and()
            .issuedAt(Date(System.currentTimeMillis()))
            .expiration(Date(Date().time + TimeUnit.MINUTES.toMillis((if (isRefreshToken) refreshTokenExpirationTime else accessTokenExpirationTime)!!)))
            .signWith(Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey)))
            .compact()
    }

    private fun isTokenValid(token: String): Boolean {
        val claims = extractAllClaims(token)
        return claims.expiration.after(Date(System.currentTimeMillis()))
    }

    fun getUser(token: String): User? {
        val userId = extractAllClaims(token).get("id", Long::class.java)
        return userService.findById(userId)
    }

    private fun extractAllClaims(token: String): Claims {
        return parser!!.parseSignedClaims(token.replace(tokenPrefix!!, "")).payload
    }
}