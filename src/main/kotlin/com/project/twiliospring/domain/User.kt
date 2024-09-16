package com.project.twiliospring.domain

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

@Entity(name = "customer")
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long?,
    @JvmField val username: String,
    @JvmField val password: String,
    val email: String,
    val phoneNumber: String
) : UserDetails {
    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return mutableListOf(SimpleGrantedAuthority("ROLE_USER"))
    }

    override fun getPassword(): String {
        return password
    }

    override fun getUsername(): String {
        return username
    }
}