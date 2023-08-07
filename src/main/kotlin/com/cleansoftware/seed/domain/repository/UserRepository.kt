package com.cleansoftware.seed.domain.repository

import com.cleansoftware.seed.domain.entities.User
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*


interface UserRepository : JpaRepository<User, Long?> {
    fun findByUsername(username: String): Optional<User>
}
