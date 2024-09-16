package com.project.twiliospring.security

import com.project.twiliospring.service.UserService
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Component

@Component
class CustomUserDetailService(
    private val userService: UserService
) : UserDetailsService {
    override fun loadUserByUsername(username: String): UserDetails {
        return userService.findByUsername(username) ?: throw UsernameNotFoundException("User not found")
    }
}