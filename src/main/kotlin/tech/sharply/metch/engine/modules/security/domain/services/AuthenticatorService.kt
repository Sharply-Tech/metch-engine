package tech.sharply.metch.engine.modules.security.domain.services

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.oauth2.jwt.JwtDecoder
import org.springframework.stereotype.Service
import tech.sharply.metch.engine.modules.security.domain.model.UserPrincipal
import tech.sharply.metch.engine.modules.security.infrastructure.constants.SecurityConstants

@Service
class AuthenticatorService(
    @Autowired
    val userDetailsService: UserDetailsService,
    @Autowired
    val jwtDecoder: JwtDecoder
) {

    fun getAuthenticationOrFail(jwt: String?) : UsernamePasswordAuthenticationToken {
        if (jwt == null || jwt.trim { it <= ' ' }.isEmpty()) {
            throw AuthenticationCredentialsNotFoundException("No JWT token present!")
        }
//        val user: String = JWT.require(Algorithm.HMAC512(SecurityConstants.SECRET.getBytes()))
                val user: String = jwtDecoder.decode(jwt);
            .build()
            .verify(jwt.replace(SecurityConstants.TOKEN_PREFIX, ""))
            .getSubject()
        val userPrincipal: UserPrincipal = userDetailsService.loadUserByUsername(user) as UserPrincipal

        return UsernamePasswordAuthenticationToken(
            userPrincipal,
            null,
            userPrincipal.authorities
        )
    }

}