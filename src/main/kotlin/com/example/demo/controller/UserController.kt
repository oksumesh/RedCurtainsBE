package com.example.demo.controller

import com.example.demo.entity.User
import com.example.demo.entity.LoyaltyTier
import com.example.demo.service.UserService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api/users")
class UserController(private val userService: UserService) {

    // Get all users
    @GetMapping
    fun getAllUsers(): ResponseEntity<List<User>> {
        val users = userService.getAllUsers()
        return ResponseEntity.ok(users)
    }

    // Get user by ID
    @GetMapping("/{id}")
    fun getUserById(@PathVariable id: Long): ResponseEntity<User> {
        val user = userService.findById(id)
        return if (user.isPresent) {
            ResponseEntity.ok(user.get())
        } else {
            ResponseEntity.notFound().build()
        }
    }

    // Get user by email
    @GetMapping("/email/{email}")
    fun getUserByEmail(@PathVariable email: String): ResponseEntity<User> {
        val user = userService.findByEmail(email)
        return if (user.isPresent) {
            ResponseEntity.ok(user.get())
        } else {
            ResponseEntity.notFound().build()
        }
    }

    // Create new user
    @PostMapping
    fun createUser(@RequestBody request: CreateUserRequest): ResponseEntity<User> {
        return try {
            val user = userService.createUser(
                email = request.email,
                password = request.password,
                firstName = request.firstName,
                lastName = request.lastName
            )
            ResponseEntity.ok(user)
        } catch (e: IllegalArgumentException) {
            ResponseEntity.badRequest().build()
        }
    }

    // Update user
    @PutMapping("/{id}")
    fun updateUser(@PathVariable id: Long, @RequestBody request: UpdateUserRequest): ResponseEntity<User> {
        return try {
            val existingUser = userService.findById(id)
            if (existingUser.isPresent) {
                val updatedUser = existingUser.get().copy(
                    firstName = request.firstName,
                    lastName = request.lastName,
                    phoneNumber = request.phoneNumber
                )
                val savedUser = userService.updateUser(updatedUser)
                ResponseEntity.ok(savedUser)
            } else {
                ResponseEntity.notFound().build()
            }
        } catch (e: IllegalArgumentException) {
            ResponseEntity.badRequest().build()
        }
    }

    // Add loyalty points
    @PostMapping("/{id}/loyalty-points")
    fun addLoyaltyPoints(@PathVariable id: Long, @RequestBody request: AddLoyaltyPointsRequest): ResponseEntity<User> {
        return try {
            val user = userService.addLoyaltyPoints(id, request.points)
            ResponseEntity.ok(user)
        } catch (e: IllegalArgumentException) {
            ResponseEntity.notFound().build()
        }
    }

    // Verify email
    @PostMapping("/{id}/verify-email")
    fun verifyEmail(@PathVariable id: Long): ResponseEntity<User> {
        return try {
            val user = userService.verifyEmail(id)
            ResponseEntity.ok(user)
        } catch (e: IllegalArgumentException) {
            ResponseEntity.notFound().build()
        }
    }

    // Deactivate user
    @PostMapping("/{id}/deactivate")
    fun deactivateUser(@PathVariable id: Long): ResponseEntity<User> {
        return try {
            val user = userService.deactivateUser(id)
            ResponseEntity.ok(user)
        } catch (e: IllegalArgumentException) {
            ResponseEntity.notFound().build()
        }
    }

    // Get users by loyalty tier
    @GetMapping("/loyalty-tier/{tier}")
    fun getUsersByLoyaltyTier(@PathVariable tier: LoyaltyTier): ResponseEntity<List<User>> {
        val users = userService.getUsersByLoyaltyTier(tier)
        return ResponseEntity.ok(users)
    }

    // Get active users
    @GetMapping("/active")
    fun getActiveUsers(): ResponseEntity<List<User>> {
        val users = userService.getActiveUsers()
        return ResponseEntity.ok(users)
    }

    // Check if user exists
    @GetMapping("/exists/{email}")
    fun checkUserExists(@PathVariable email: String): ResponseEntity<Map<String, Boolean>> {
        val exists = userService.userExists(email)
        return ResponseEntity.ok(mapOf("exists" to exists))
    }

    // Health check endpoint
    @GetMapping("/health")
    fun healthCheck(): ResponseEntity<Map<String, String>> {
        return ResponseEntity.ok(mapOf("status" to "UP", "service" to "User Service"))
    }
}

// Request DTOs
data class CreateUserRequest(
    val email: String,
    val password: String,
    val firstName: String? = null,
    val lastName: String? = null
)

data class UpdateUserRequest(
    val firstName: String? = null,
    val lastName: String? = null,
    val phoneNumber: String? = null
)

data class AddLoyaltyPointsRequest(
    val points: Int
) 