package com.example.demo.repository

import com.example.demo.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface UserRepository : JpaRepository<User, Long> {
    
    // Find user by email
    fun findByEmail(email: String): Optional<User>
    
    // Check if user exists by email
    fun existsByEmail(email: String): Boolean
    
    // Find active users
    fun findByIsActiveTrue(): List<User>
    
    // Find users by loyalty tier
    fun findByLoyaltyTier(loyaltyTier: com.example.demo.entity.LoyaltyTier): List<User>
    
    // Find users with email verified
    fun findByEmailVerifiedTrue(): List<User>
    
    // Custom query to find users by email containing pattern
    @Query("SELECT u FROM User u WHERE u.email LIKE %:emailPattern%")
    fun findByEmailContaining(@Param("emailPattern") emailPattern: String): List<User>
    
    // Custom query to find users created after a specific date
    @Query("SELECT u FROM User u WHERE u.createdAt >= :date")
    fun findByCreatedAtAfter(@Param("date") date: java.time.LocalDateTime): List<User>
    
    // Custom query to find users with loyalty points greater than threshold
    @Query("SELECT u FROM User u WHERE u.loyaltyPoints >= :minPoints")
    fun findByLoyaltyPointsGreaterThanEqual(@Param("minPoints") minPoints: Int): List<User>
} 