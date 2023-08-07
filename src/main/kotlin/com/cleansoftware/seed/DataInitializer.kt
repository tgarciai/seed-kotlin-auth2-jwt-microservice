package com.cleansoftware.seed

import com.cleansoftware.seed.domain.entities.User
import com.cleansoftware.seed.domain.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import java.util.function.Consumer

@Component
class DataInitializer : CommandLineRunner {
    @Autowired
    private val users: UserRepository? = null

    @Autowired
    private val passwordEncoder: PasswordEncoder? = null
    override fun run(vararg args: String) {
        val user1 = User(
            username = "user",
            password = passwordEncoder?.encode("password"),
            roles = mutableListOf("ROLE_USER")
        )
        users?.save(user1)
        val user2 = User(
            username = "admin",
            password = passwordEncoder?.encode("password"),
            roles = mutableListOf("ROLE_ADMIN")
        )
        users?.save(user2)

        users!!.findAll().forEach(Consumer { v: User ->
            println(v.authorities)
        })
    }
}
