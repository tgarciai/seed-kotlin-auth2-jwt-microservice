package com.cleansoftware.seed.config

import com.cleansoftware.seed.domain.repository.UserRepository
import com.cleansoftware.seed.infra.security.JwtTokenAuthenticationFilter
import com.cleansoftware.seed.infra.security.JwtTokenProvider
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.DisabledException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configurers.*
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.factory.PasswordEncoderFactories
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.HttpStatusEntryPoint
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter


@Configuration
class SecurityConfig {
    @Bean
    @Throws(Exception::class)
    fun springWebFilterChain(
        http: HttpSecurity,
        tokenProvider: JwtTokenProvider?
    ): SecurityFilterChain {
        return http
            .httpBasic { obj: HttpBasicConfigurer<HttpSecurity> -> obj.disable() }
            .csrf { obj: CsrfConfigurer<HttpSecurity> -> obj.disable() }
            .sessionManagement { c: SessionManagementConfigurer<HttpSecurity?> ->
                c.sessionCreationPolicy(
                    SessionCreationPolicy.STATELESS
                )
            }
            .exceptionHandling { c: ExceptionHandlingConfigurer<HttpSecurity?> ->
                c.authenticationEntryPoint(
                    HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)
                )
            }
            .authorizeHttpRequests {
                it.requestMatchers("/auth/signin").permitAll()
                    .requestMatchers(HttpMethod.GET, "/user/**").hasRole("USER")
                    .requestMatchers(HttpMethod.GET, "/admin/**").hasRole("ADMIN")
                    .anyRequest().authenticated()
            }
            .addFilterBefore(
                JwtTokenAuthenticationFilter(tokenProvider!!),
                UsernamePasswordAuthenticationFilter::class.java
            )
            .build()
    }

    @Bean
    fun userDetailsService(users: UserRepository): UserDetailsService {
        return UserDetailsService { username: String ->
            users.findByUsername(username).orElseThrow { UsernameNotFoundException("$username username not found") }
        }
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder()
    }

    @Bean
    fun customAuthenticationManager(
        userDetailsService: UserDetailsService,
        encoder: PasswordEncoder
    ): AuthenticationManager {
        return AuthenticationManager { authentication: Authentication ->
            val username = authentication.principal.toString() + ""
            val password = authentication.credentials.toString() + ""
            val user = userDetailsService.loadUserByUsername(username)
            if (!encoder.matches(password, user.password)) {
                throw BadCredentialsException("Bad credentials")
            }
            if (!user.isEnabled) {
                throw DisabledException("User account is not active")
            }
            UsernamePasswordAuthenticationToken(username, null, user.authorities)
        }
    }
}
