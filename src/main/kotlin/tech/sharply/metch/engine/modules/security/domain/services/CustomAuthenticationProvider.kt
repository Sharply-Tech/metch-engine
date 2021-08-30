package tech.sharply.metch.engine.modules.security.domain.services

import org.jboss.aerogear.security.otp.Totp
import org.slf4j.LoggerFactory
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import tech.sharply.metch.engine.modules.security.domain.exceptions.InvalidTotpException
import tech.sharply.metch.engine.modules.security.domain.exceptions.MissingTotpException
import tech.sharply.metch.engine.modules.security.domain.model.CustomUsernamePasswordAuthenticationToken
import tech.sharply.metch.engine.modules.security.domain.model.UserPrincipal

class CustomAuthenticationProvider(private val userDetailsService: UserDetailsServiceImpl) :
    DaoAuthenticationProvider() {

    init {
        setUserDetailsService(userDetailsService)
    }

    companion object {
        private val log = LoggerFactory.getLogger(CustomAuthenticationProvider.javaClass)
    }

    @Throws(AuthenticationException::class)
    override fun authenticate(auth: Authentication): Authentication {
        require(auth is CustomUsernamePasswordAuthenticationToken) {
            "CustomAuthenticationProvider only supports " +
                    "CustomUsernamePasswordAuthenticationToken"
        }
        val user: UserPrincipal
        try {
            user = userDetailsService.loadUserByUsername(auth.name) as UserPrincipal
        } catch (ex: Exception) {
            log.error("")
            throw BadCredentialsException("Invalid username/password!")
        }
        val result = super.authenticate(auth)

        if (user.usesMultiFactorAuth()) {
            val verificationCode: String = auth.verificationCode
                ?: throw MissingTotpException("The verification code is missing!")
            val totp = Totp(user.multiFactorSecret)
            if (!isValidLong(verificationCode) || !totp.verify(verificationCode)) {
                throw InvalidTotpException("Invalid verification code!")
            }
        }
        return UsernamePasswordAuthenticationToken(user, result.credentials, result.authorities)
    }

    override fun supports(authentication: Class<*>): Boolean {
        return authentication == UsernamePasswordAuthenticationToken::class.java || authentication == CustomUsernamePasswordAuthenticationToken::class.java
    }

    private fun isValidLong(code: String): Boolean {
        try {
            code.toLong()
        } catch (e: NumberFormatException) {
            return false
        }
        return true
    }

}
