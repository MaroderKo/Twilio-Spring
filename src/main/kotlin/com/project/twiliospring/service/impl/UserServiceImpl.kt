package com.project.twiliospring.service.impl

import com.project.twiliospring.domain.User
import com.project.twiliospring.repository.UserRepository
import com.project.twiliospring.service.UserService
import org.springframework.stereotype.Service

@Service
class UserServiceImpl(
    private val userRepository: UserRepository
) : UserService {
    override fun findById(id: Long): User? = userRepository.findById(id).orElse(null)
    override fun findByUsername(username: String): User? = userRepository.findByUsername(username)
    override fun findByUsernameAndPassword(username: String, password: String): User? =
        userRepository.findByUsernameAndPassword(username, password)

    override fun findByEmail(email: String): User? = userRepository.findByEmail(email)
    override fun save(user: User): User = userRepository.save(user)
    override fun update(user: User): User = userRepository.save(user)
    override fun delete(user: User): User = userRepository.save(user)
}