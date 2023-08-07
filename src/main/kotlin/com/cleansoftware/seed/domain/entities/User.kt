package com.cleansoftware.seed.domain.entities

import jakarta.persistence.*
import jakarta.validation.constraints.NotEmpty
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.util.*
import java.util.stream.Collectors

@Entity
@Table(name = "users")
class User(
    @NotEmpty
    private val username: String? = null,

    @NotEmpty
    private val password: String? = null,

    @ElementCollection(fetch = FetchType.EAGER)
    private val roles: List<String> = ArrayList()
) : UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: UUID? = null
    override fun getAuthorities(): Collection<GrantedAuthority?> {
        return roles.stream().map { role: String? ->
            SimpleGrantedAuthority(
                role
            )
        }.collect(Collectors.toList())
    }

    override fun getPassword(): String {
        return password!!
    }

    override fun getUsername(): String {
        return username!!
    }

    override fun isAccountNonExpired(): Boolean {
        return true
    }

    override fun isAccountNonLocked(): Boolean {
        return true
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    override fun isEnabled(): Boolean {
        return true
    }
}
