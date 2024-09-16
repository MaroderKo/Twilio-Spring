package com.project.twiliospring.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.invoke
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
                authorize("/message", authenticated)
                authorize(anyRequest, denyAll)
            }
            addFilterAfter<BasicAuthenticationFilter>(jwtFilter)
        }

        return http.build()

    }
}