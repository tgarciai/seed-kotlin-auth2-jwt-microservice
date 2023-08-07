package com.cleansoftware.seed.infra.security

import jakarta.annotation.Priority
import org.springframework.stereotype.Component

@Component
@Priority(1)
class JwtProperties {
    val secretKey: String =  System.getenv("JWT_SECRET_KEY") ?: "the-secret-key-1234567890-omg"
    val validityInMs: Long = 3600000
}
