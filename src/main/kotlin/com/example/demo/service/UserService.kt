package com.example.demo.service

import com.example.demo.entity.User
import com.example.demo.entity.LoyaltyTier
import com.example.demo.repository.UserRepository
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.*

@Service
class UserService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder
) {

    // Create a new user
    fun createUser(email: String, password: String, firstName: String? = null, lastName: String? = null): User {
        if (userRepository.existsByEmail(email)) {
            throw IllegalArgumentException("User with email $email already exists")
        }

        val encodedPassword = passwordEncoder.encode(password)
        val user = User(
            email = email,
            password = encodedPassword,
            firstName = firstName,
            lastName = lastName,
            createdAt = LocalDateTime.now()
        )

        return userRepository.save(user)
    }

    // Find user by email
    fun findByEmail(email: String): Optional<User> {
        return userRepository.findByEmail(email)
    }

    // Find user by ID
    fun findById(id: Long): Optional<User> {
        return userRepository.findById(id)
    }

    // Get all users
    fun getAllUsers(): List<User> {
        return userRepository.findAll()
    }

    // Update user
    fun updateUser(user: User): User {
        val existingUser = userRepository.findById(user.id)
            .orElseThrow { IllegalArgumentException("User not found with ID: ${user.id}") }

        val updatedUser = existingUser.copy(
            firstName = user.firstName ?: existingUser.firstName,
            lastName = user.lastName ?: existingUser.lastName,
            phoneNumber = user.phoneNumber ?: existingUser.phoneNumber,
            updatedAt = LocalDateTime.now()
        )

        return userRepository.save(updatedUser)
    }

    // Update last login time
    fun updateLastLogin(userId: Long): User {
        val user = userRepository.findById(userId)
            .orElseThrow { IllegalArgumentException("User not found with ID: $userId") }

        val updatedUser = user.copy(lastLoginAt = LocalDateTime.now())
        return userRepository.save(updatedUser)
    }

    // Verify email
    fun verifyEmail(userId: Long): User {
        val user = userRepository.findById(userId)
            .orElseThrow { IllegalArgumentException("User not found with ID: $userId") }

        val updatedUser = user.copy(
            emailVerified = true,
            updatedAt = LocalDateTime.now()
        )

        return userRepository.save(updatedUser)
    }

    // Add loyalty points
    fun addLoyaltyPoints(userId: Long, points: Int): User {
        val user = userRepository.findById(userId)
            .orElseThrow { IllegalArgumentException("User not found with ID: $userId") }

        val newPoints = user.loyaltyPoints + points
        val newTier = calculateLoyaltyTier(newPoints)

        val updatedUser = user.copy(
            loyaltyPoints = newPoints,
            loyaltyTier = newTier,
            updatedAt = LocalDateTime.now()
        )

        return userRepository.save(updatedUser)
    }

    // Calculate loyalty tier based on points
    private fun calculateLoyaltyTier(points: Int): LoyaltyTier {
        return when {
            points >= 10000 -> LoyaltyTier.PLATINUM
            points >= 5000 -> LoyaltyTier.GOLD
            points >= 1000 -> LoyaltyTier.SILVER
            else -> LoyaltyTier.BRONZE
        }
    }

    // Deactivate user
    fun deactivateUser(userId: Long): User {
        val user = userRepository.findById(userId)
            .orElseThrow { IllegalArgumentException("User not found with ID: $userId") }

        val updatedUser = user.copy(
            isActive = false,
            updatedAt = LocalDateTime.now()
        )

        return userRepository.save(updatedUser)
    }

    // Get users by loyalty tier
    fun getUsersByLoyaltyTier(tier: LoyaltyTier): List<User> {
        return userRepository.findByLoyaltyTier(tier)
    }

    // Get active users
    fun getActiveUsers(): List<User> {
        return userRepository.findByIsActiveTrue()
    }

    // Check if user exists
    fun userExists(email: String): Boolean {
        return userRepository.existsByEmail(email)
    }
} 