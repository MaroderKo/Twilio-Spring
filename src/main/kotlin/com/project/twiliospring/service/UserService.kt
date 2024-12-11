package com.project.twiliospring.service

import com.project.twiliospring.domain.User

interface UserService {
    fun findById(id: Long): User?
    fun findByUsername(username: String): User?
    fun findByUsernameAndPassword(username: String, password: String): User?
    fun findByEmail(email: String): User?
    fun save(user: User): User
    fun update(user: User): User
    fun delete(user: User)
}