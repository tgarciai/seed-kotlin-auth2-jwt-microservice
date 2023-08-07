package com.cleansoftware.seed.domain.dto

import java.io.Serializable


class AuthenticationRequest(
    private val username: String,
    private val password: String
) : Serializable {
    fun getUserName(): String {
        return username
    }

    fun getPassWord(): String {
        return password
    }

    companion object {
        private const val serialVersionUID = -11111111111L
    }
}
