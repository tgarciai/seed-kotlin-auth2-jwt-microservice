package com.cleansoftware.seed.security

import jakarta.servlet.FilterChain
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import jakarta.servlet.http.HttpServletRequest
import org.springframework.web.filter.GenericFilterBean
import org.springframework.http.HttpHeaders
import org.springframework.security.authentication.AnonymousAuthenticationToken
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.util.StringUtils

class JwtTokenAuthenticationFilter(private val jwtTokenProvider: JwtTokenProvider) : GenericFilterBean() {

    companion object {
        const val HEADER_PREFIX = "Bearer "
    }

    override fun doFilter(req: ServletRequest, res: ServletResponse, filterChain: FilterChain) {
        val token = resolveToken(req as HttpServletRequest)


        if (token != null && jwtTokenProvider.validateToken(token)) {
            val auth = jwtTokenProvider.getAuthentication(token)

            if (auth != null && auth !is AnonymousAuthenticationToken) {
                val context: SecurityContext = SecurityContextHolder.createEmptyContext()
                context.authentication = auth
                SecurityContextHolder.setContext(context)
            }
        }

        filterChain.doFilter(req, res)
    }

    private fun resolveToken(request: HttpServletRequest): String? {
        val bearerToken = request.getHeader(HttpHeaders.AUTHORIZATION)
        return if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(HEADER_PREFIX)) {
            bearerToken.substring(HEADER_PREFIX.length)
        } else null
    }
}
