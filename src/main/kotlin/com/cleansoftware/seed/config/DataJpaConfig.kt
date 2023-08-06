package com.cleansoftware.seed.config


import com.cleansoftware.seed.domain.Username
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.domain.AuditorAware
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import java.util.*


@Configuration
@EnableJpaAuditing
class DataJpaConfig {
    @Bean
    fun auditor(): AuditorAware<Username> {
        return AuditorAware {
            Optional.ofNullable(
                SecurityContextHolder.getContext()
            )
                .map { obj: SecurityContext -> obj.authentication }
                .filter { obj: Authentication -> obj.isAuthenticated }
                .map { obj: Authentication -> obj.principal }
                .map { obj: Any? ->
                    UserDetails::class.java.cast(
                        obj
                    )
                }
                .map { u: UserDetails ->
                    Username(
                        u.username
                    )
                }
        }
    }
}
