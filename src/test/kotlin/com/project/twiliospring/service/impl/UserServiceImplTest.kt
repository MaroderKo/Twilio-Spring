package com.project.twiliospring.service.impl

import com.project.twiliospring.domain.User
import com.project.twiliospring.repository.UserRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import java.util.*

@SpringBootTest
class UserServiceImplTest {

    @MockBean
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var userService: UserServiceImpl

    private val testUser: User =
        User(
            id = 1L,
            username = "testuser",
            email = "testuser@example.com",
            password = "password",
            phoneNumber = "+380670000000"
        )

    @Test
    fun `test findById - user exists`() {
        `when`(userRepository.findById(1L)).thenReturn(Optional.of(testUser))
        val result = userService.findById(1L)
        assertNotNull(result)
    }

    @Test
    fun `test findById - user does not exist`() {
        `when`(userRepository.findById(1L)).thenReturn(Optional.empty())
        val result = userService.findById(1L)
        assertNull(result)
    }

    @Test
    fun `test findByUsername - user exists`() {
        `when`(userRepository.findByUsername("testuser")).thenReturn(testUser)
        val result = userService.findByUsername("testuser")
        assertNotNull(result)
    }

    @Test
    fun `test findByUsername - user does not exist`() {
        `when`(userRepository.findByUsername("nonexistent")).thenReturn(null)
        val result = userService.findByUsername("nonexistent")
        assertNull(result)
    }

    @Test
    fun `test findByUsernameAndPassword - valid credentials`() {
        `when`(userRepository.findByUsernameAndPassword("testuser", "password")).thenReturn(testUser)
        val result = userService.findByUsernameAndPassword("testuser", "password")
        assertNotNull(result)
        assertEquals("testuser", result?.username)
        assertEquals("testuser@example.com", result?.email)
    }

    @Test
    fun `test findByUsernameAndPassword - invalid credentials`() {
        `when`(userRepository.findByUsernameAndPassword("testuser", "wrongpassword")).thenReturn(null)
        val result = userService.findByUsernameAndPassword("testuser", "wrongpassword")
        assertNull(result)
    }

    @Test
    fun `test findByEmail - user exists`() {
        `when`(userRepository.findByEmail("testuser@example.com")).thenReturn(testUser)
        val result = userService.findByEmail("testuser@example.com")
        assertNotNull(result)
    }

    @Test
    fun `test findByEmail - user does not exist`() {
        `when`(userRepository.findByEmail("nonexistent@example.com")).thenReturn(null)
        val result = userService.findByEmail("nonexistent@example.com")
        assertNull(result)
    }

    @Test
    fun `test save - valid user`() {
        val user = testUser.copy(id = null)
        `when`(userRepository.save(user)).thenReturn(testUser)

        val result = userService.save(user)

        assertNotNull(result)
        assertEquals(1L, result.id)
        assertEquals("testuser", result.username)
        assertEquals("testuser@example.com", result.email)
        assertEquals("password", result.password)
        assertEquals("+380670000000", result.phoneNumber)
    }

    @Test
    fun `test update - valid user`() {
        val user = User(
            id = 1L,
            username = "updatedUser",
            email = "updated@example.com",
            password = "newpassword",
            phoneNumber = null
        )
        `when`(userRepository.save(user)).thenReturn(user)

        val result = userService.update(user)

        assertNotNull(result)
        assertEquals(1L, result.id)
        assertEquals("updatedUser", result.username)
        assertEquals("updated@example.com", result.email)
        assertEquals("newpassword", result.password)
    }

    @Test
    fun `test delete - valid user`() {
        doNothing().`when`(userRepository).delete(testUser)
        userService.delete(testUser)
        verify(userRepository, times(1)).delete(testUser)
        verifyNoMoreInteractions(userRepository)
    }
}