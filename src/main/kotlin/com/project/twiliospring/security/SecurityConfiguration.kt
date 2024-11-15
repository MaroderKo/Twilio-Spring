package com.project.twiliospring.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.AuthenticationServiceException
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.invoke
import org.springframework.security.core.Authentication
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter


@Configuration
@EnableWebSecurity
class SecurityConfiguration {

    @Bean
    fun securityWebFilterChain(http: HttpSecurity, jwtFilter: JWTFilter): SecurityFilterChain {
        http.invoke {
            authorizeRequests {
                authorize("/", authenticated)
                authorize("/login/**", permitAll)
                authorize("/login/code", permitAll)
                authorize("/logout", permitAll)
                authorize("/user", authenticated)
                authorize(anyRequest, denyAll)
            }
            csrf { disable() }
            cors { disable() }
            addFilterAfter<BasicAuthenticationFilter>(jwtFilter)
        }

        return http.build()

    }

    // made spring security not creating user automatically
    @Bean
    fun noopAuthenticationManager(): AuthenticationManager {
        return AuthenticationManager { _: Authentication? ->
            throw AuthenticationServiceException("Authentication is disabled")
        }
    }
}