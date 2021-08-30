package tech.sharply.metch.engine.modules.security.domain.services

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service
import tech.sharply.metch.engine.modules.security.domain.model.UserPrincipal
import tech.sharply.metch.engine.modules.security.infrastructure.constants.SecurityConstants

@Service
class AuthenticatorService(
    @Autowired
    val userDetailsService: UserDetailsService
) {

    fun getAuthenticationOrFail(jwt: String?): UsernamePasswordAuthenticationToken {
        if (jwt == null || jwt.trim { it <= ' ' }.isEmpty()) {
            throw AuthenticationCredentialsNotFoundException("No JWT token present!")
        }
        val user: String = JWT.require(Algorithm.HMAC512(SecurityConstants.SECRET.encodeToByteArray()))
            .build()
            .verify(jwt.replace(SecurityConstants.TOKEN_PREFIX, ""))
            .subject
        val userPrincipal: UserPrincipal = userDetailsService.loadUserByUsername(user) as UserPrincipal

        return UsernamePasswordAuthenticationToken(
            userPrincipal,
            null,
            userPrincipal.authorities
        )
    }

}