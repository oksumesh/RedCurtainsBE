package com.example.demo.controller

import com.example.demo.entity.User
import com.example.demo.service.UserService
import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime

@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val userService: UserService,
    private val passwordEncoder: PasswordEncoder
) {

    // User registration
    @PostMapping("/register")
    fun register(@RequestBody request: RegisterRequest): ResponseEntity<AuthResponse> {
        return try {
            // Check if user already exists
            if (userService.userExists(request.email)) {
                return ResponseEntity.badRequest().body(
                    AuthResponse(
                        success = false,
                        message = "User with this email already exists"
                    )
                )
            }

            // Create new user
            val user = userService.createUser(
                email = request.email,
                password = request.password,
                firstName = request.firstName,
                lastName = request.lastName
            )

            // Generate a simple token (in production, use JWT)
            val token = generateToken(user.email)

            val response = AuthResponse(
                success = true,
                message = "Registration successful",
                token = token,
                user = user,
                expiresIn = 3600L // 1 hour
            )

            ResponseEntity.ok(response)
        } catch (e: IllegalArgumentException) {
            // Handle specific validation errors (like duplicate email)
            ResponseEntity.badRequest().body(
                AuthResponse(
                    success = false,
                    message = e.message ?: "Registration failed"
                )
            )
        } catch (e: Exception) {
            // Handle other unexpected errors
            ResponseEntity.badRequest().body(
                AuthResponse(
                    success = false,
                    message = "Registration failed: ${e.message}"
                )
            )
        }
    }

    // User login
    @PostMapping("/login")
    fun login(@RequestBody request: LoginRequest): ResponseEntity<AuthResponse> {
        return try {
            // Find user by email
            val userOptional = userService.findByEmail(request.email)
            if (userOptional.isEmpty) {
                return ResponseEntity.badRequest().body(
                    AuthResponse(
                        success = false,
                        message = "Invalid email or password"
                    )
                )
            }

            val user = userOptional.get()

            // Check if user is active
            if (!user.isActive) {
                return ResponseEntity.badRequest().body(
                    AuthResponse(
                        success = false,
                        message = "Account is deactivated"
                    )
                )
            }

            // Verify password
            if (!passwordEncoder.matches(request.password, user.password)) {
                return ResponseEntity.badRequest().body(
                    AuthResponse(
                        success = false,
                        message = "Invalid email or password"
                    )
                )
            }

            // Update last login
            userService.updateLastLogin(user.id!!)

            // Generate token
            val token = generateToken(user.email)

            val response = AuthResponse(
                success = true,
                message = "Login successful",
                token = token,
                user = user,
                expiresIn = if (request.rememberMe) 86400L else 3600L // 24 hours or 1 hour
            )

            ResponseEntity.ok(response)
        } catch (e: Exception) {
            ResponseEntity.badRequest().body(
                AuthResponse(
                    success = false,
                    message = "Login failed: ${e.message}"
                )
            )
        }
    }

    // Logout
    @PostMapping("/logout")
    fun logout(@RequestHeader("Authorization") authHeader: String): ResponseEntity<Map<String, String>> {
        // In a real implementation, you would invalidate the token
        // For now, just return success
        return ResponseEntity.ok(mapOf("message" to "Logout successful"))
    }

    // Refresh token
    @PostMapping("/refresh")
    fun refreshToken(@RequestBody request: RefreshTokenRequest): ResponseEntity<AuthResponse> {
        // In a real implementation, you would validate the refresh token
        // For now, just return a new token
        return ResponseEntity.ok(
            AuthResponse(
                success = true,
                message = "Token refreshed",
                token = generateToken("user@example.com"),
                expiresIn = 3600L
            )
        )
    }

    private fun generateToken(email: String): String {
        // Simple token generation (in production, use JWT)
        return "token_${email}_${System.currentTimeMillis()}"
    }
}

// Request/Response DTOs
data class RegisterRequest(
    val email: String,
    val password: String,
    val firstName: String,
    val lastName: String,
    val phoneNumber: String? = null
)

data class LoginRequest(
    val email: String,
    val password: String,
    val rememberMe: Boolean = false
)

data class RefreshTokenRequest(
    val refreshToken: String
)

data class AuthResponse(
    val success: Boolean,
    val message: String,
    val token: String? = null,
    val refreshToken: String? = null,
    val user: User? = null,
    val expiresIn: Long? = null
) 