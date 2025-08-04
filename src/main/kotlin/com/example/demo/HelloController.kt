package com.example.demo

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class HelloController {
    
    @GetMapping("/")
    fun home(): String {
        return "Welcome to RedCurtains Backend! 🎬"
    }
    
    @GetMapping("/hello")
    fun hello(): String {
        return "Hello from Spring Boot + Kotlin! 🚀"
    }
    
    @GetMapping("/api/status")
    fun status(): Map<String, Any> {
        return mapOf(
            "status" to "running",
            "service" to "RedCurtains Backend",
            "version" to "1.0.0",
            "timestamp" to System.currentTimeMillis()
        )
    }
} 