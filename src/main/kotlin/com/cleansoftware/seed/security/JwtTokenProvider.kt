package com.cleansoftware.seed.security

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import jakarta.annotation.PostConstruct
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.AuthorityUtils
import org.springframework.security.core.userdetails.User
import org.springframework.security.oauth2.jwt.JwtException
import org.springframework.stereotype.Component
import java.nio.charset.StandardCharsets
import java.util.*
import java.util.stream.Collectors.joining
import javax.crypto.SecretKey


@Component
class JwtTokenProvider {
    @Autowired
    private var jwtProperties: JwtProperties? = null
    private var secretKey: SecretKey? = null
    @PostConstruct
    fun init() {
        val secret = Base64.getEncoder().encodeToString(jwtProperties!!.secretKey.toByteArray())
        secretKey = Keys.hmacShaKeyFor(secret.toByteArray(StandardCharsets.UTF_8))
    }

    fun createToken(authentication: Authentication): String {
        val username = authentication.name
        val authorities = authentication.authorities
        val claims = Jwts.claims().setSubject(username)
        if (!authorities.isEmpty()) {
            claims[AUTHORITIES_KEY] = authorities.stream().map { obj: GrantedAuthority -> obj.authority }
                .collect(joining(","))
        }
        val now = Date()
        val validity = Date(now.time + jwtProperties!!.validityInMs)
        return Jwts.builder()
            .setClaims(claims)
            .setIssuedAt(now)
            .setExpiration(validity)
            .signWith(secretKey, SignatureAlgorithm.HS256)
            .compact()
    }

    fun getAuthentication(token: String?): Authentication {
        val claims = Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).body
        val authoritiesClaim = claims[AUTHORITIES_KEY]
        val authorities: kotlin.collections.Collection<GrantedAuthority> =
            if (authoritiesClaim == null) AuthorityUtils.NO_AUTHORITIES else AuthorityUtils.commaSeparatedStringToAuthorityList(
                authoritiesClaim.toString()
            )
        val principal = User(claims.subject, "", authorities)
        return UsernamePasswordAuthenticationToken(principal, token, authorities)
    }

    fun validateToken(token: String?): Boolean {
        try {
            val claims = Jwts
                .parserBuilder().setSigningKey(secretKey).build()
                .parseClaimsJws(token)
            //  parseClaimsJws will check expiration date. No need do here.
            // JwtTokenProvider.log.info("expiration date: {}", claims.body.expiration)
            return true
        } catch (e: JwtException) {
            // JwtTokenProvider.log.error("Invalid JWT token: {}", e.message)
        } catch (e: IllegalArgumentException) {
            // JwtTokenProvider.log.error("Invalid JWT token: {}", e.message)
        }
        return false
    }

    companion object {
        private const val AUTHORITIES_KEY = "roles"
    }
}
