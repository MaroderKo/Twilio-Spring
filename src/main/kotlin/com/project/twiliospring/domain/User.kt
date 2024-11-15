package com.project.twiliospring.domain

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.persistence.*
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

@Entity(name = "customer")
@Table(
    uniqueConstraints = [
        UniqueConstraint(name = "usernameUniqConst", columnNames = ["username"]),
        UniqueConstraint(name = "emailUniqConst", columnNames = ["email"]),
        UniqueConstraint(name = "phoneNumberUniqConst", columnNames = ["phone_number"])
    ]
)
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long?,
    @JvmField @field:NotNull @field:NotBlank val username: String,
    @JvmField @field:NotNull @field:NotBlank val password: String,
    val email: String?,
    @field:JsonProperty("phone_number") val phoneNumber: String?
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