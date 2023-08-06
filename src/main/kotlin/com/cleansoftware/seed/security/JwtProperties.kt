package com.cleansoftware.seed.security

import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component

@Component
class JwtProperties {
    val secretKey = "rzxlszyykpbgqcflzxsqcysyhljt"

    // validity in milliseconds
    val validityInMs: Long = 3600000 // 1h
}
