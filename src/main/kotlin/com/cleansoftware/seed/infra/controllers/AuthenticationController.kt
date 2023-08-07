package com.cleansoftware.seed.infra.controllers

import com.cleansoftware.seed.domain.dto.AuthenticationRequest
import com.cleansoftware.seed.infra.security.JwtTokenProvider
import org.springframework.beans.factory.annotation.Autowired

import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.AuthenticationException
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/auth")
class AuthenticationController {
    @Autowired
    private val authenticationManager: AuthenticationManager? = null

    @Autowired
    private val jwtTokenProvider: JwtTokenProvider? = null

    @PostMapping("/signin")
    fun signIn(@RequestBody data: AuthenticationRequest): ResponseEntity<*> {
        return try {
            val username: String = data.getUserName()
            val authentication =
                authenticationManager!!.authenticate(UsernamePasswordAuthenticationToken(username, data.getPassWord()))
            val token = jwtTokenProvider!!.createToken(authentication)
            val model: MutableMap<Any, Any> = HashMap()
            model["username"] = username
            model["token"] = token
            ResponseEntity.ok<Map<Any, Any>>(model)
        } catch (e: AuthenticationException) {
            throw BadCredentialsException("Invalid username/password supplied")
        }
    }
}
