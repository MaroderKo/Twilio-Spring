package com.project.twiliospring.repository

import com.project.twiliospring.domain.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : JpaRepository<User, Long> {
    fun findByEmail(email: String): User?
    fun findByUsername(username: String): User?
    fun findByUsernameAndPassword(username: String, password: String): User?
}