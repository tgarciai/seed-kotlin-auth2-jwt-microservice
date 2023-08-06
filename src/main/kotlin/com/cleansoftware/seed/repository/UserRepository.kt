package com.cleansoftware.seed.repository

import com.cleansoftware.seed.domain.User
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*


interface UserRepository : JpaRepository<User, Long?> {
    fun findByUsername(username: String): Optional<User>
}
