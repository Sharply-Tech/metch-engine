package tech.sharply.metch.engine.modules.security.infrastructure.filters

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.fasterxml.jackson.databind.ObjectMapper
import org.hibernate.HibernateException
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.AuthenticationFailureHandler
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter
import tech.sharply.metch.engine.building_blocks.domain.exceptions.ExceptionWithCode
import tech.sharply.metch.engine.modules.security.application.dto.LoginRequest
import tech.sharply.metch.engine.modules.security.application.dto.LoginResponse
import tech.sharply.metch.engine.modules.security.domain.exceptions.InvalidTotpException
import tech.sharply.metch.engine.modules.security.domain.exceptions.MissingTotpException
import tech.sharply.metch.engine.modules.security.domain.model.CustomUsernamePasswordAuthenticationToken
import tech.sharply.metch.engine.modules.security.domain.model.LoginEvent
import tech.sharply.metch.engine.modules.security.domain.model.LoginEventRepository
import tech.sharply.metch.engine.modules.security.domain.model.UserPrincipal
import tech.sharply.metch.engine.modules.security.domain.services.AuthenticatorService
import tech.sharply.metch.engine.modules.security.infrastructure.constants.SecurityConstants.Companion.AUTHORIZATION_HEADER
import tech.sharply.metch.engine.modules.security.infrastructure.constants.SecurityConstants.Companion.EXPIRATION_TIME
import tech.sharply.metch.engine.modules.security.infrastructure.constants.SecurityConstants.Companion.SECRET
import tech.sharply.metch.engine.modules.security.infrastructure.constants.SecurityConstants.Companion.TOKEN_PREFIX
import java.io.IOException
import java.util.*
import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class JWTAuthenticationFilter(
    val authManager: AuthenticationManager,
    private val loginHistoryRepository: LoginEventRepository
) : UsernamePasswordAuthenticationFilter() {

    init {
        setFilterProcessesUrl("/login")
        setAuthenticationFailureHandler(CustomAuthenticationFailureHandler())
    }

    @Throws(AuthenticationException::class)
    override fun attemptAuthentication(
        req: HttpServletRequest,
        res: HttpServletResponse?
    ): Authentication? {
        return try {
            val loginRequest = ObjectMapper()
                .readValue(req.inputStream, LoginRequest::class.java)
            req.setAttribute("AUTH_USERNAME", loginRequest.username)
            authenticationManager
                .authenticate(
                    CustomUsernamePasswordAuthenticationToken(
                        loginRequest.username,
                        loginRequest.password,
                        loginRequest.verificationCode
                    )
                )
        } catch (e: IOException) {
            throw RuntimeException(e)
        }
    }

    @Throws(IOException::class)
    override fun successfulAuthentication(
        req: HttpServletRequest,
        res: HttpServletResponse,
        chain: FilterChain?,
        auth: Authentication
    ) {
        val authenticatedUser: UserPrincipal = auth.principal as UserPrincipal

        // save login history
        val loginHistory = LoginEvent(authenticatedUser., getRequestIp(req))
        loginHistoryRepository.save(loginHistory)

        // generate token
        val expiresAt: Date = Date(System.currentTimeMillis() + EXPIRATION_TIME)
        val token = JWT.create()
            .withSubject(authenticatedUser.username)
            .withExpiresAt(expiresAt)
            .sign(Algorithm.HMAC512(SECRET.encodeToByteArray()))
        val loginResponse = LoginResponse(token, expiresAt)

        res.writer.write(ObjectMapper().writeValueAsString(loginResponse))
        res.setHeader("Content-Type", "application/json")
        res.writer.flush()
    }

    fun getRequestIp(request: HttpServletRequest): String? {
        var ipAddress = request.getHeader("X-FORWARDED-FOR")
        if (ipAddress == null) {
            ipAddress = request.remoteAddr
        } else if (ipAddress.contains(",")) {
            ipAddress = ipAddress.split(",").toTypedArray()[0]
        }
        return ipAddress
    }


    class CustomAuthenticationFailureHandler : AuthenticationFailureHandler {

        companion object {
            private val log = LoggerFactory.getLogger(CustomAuthenticationFailureHandler.javaClass)
        }

        @Throws(IOException::class)
        fun writeResponse(response: HttpServletResponse, entity: ResponseEntity<*>) {
            val mapper = ObjectMapper()
            response.status = entity.statusCodeValue
            response.contentType = "application/json"
            response.writer.write(mapper.writeValueAsString(entity.body))
        }

        override fun onAuthenticationFailure(
            request: HttpServletRequest?,
            response: HttpServletResponse?,
            exception: AuthenticationException?
        ) {
            val username = request!!.getAttribute("AUTH_USERNAME") as String

            if (exception == null) {
                log.error("Unknown authentication exception for user: $username")
                response?.let {
                    writeResponse(
                        it,
                        ResponseEntity.status(HttpStatus.UNAUTHORIZED).build<Any>()
                    )
                }
            } else if (exception is MissingTotpException) {
                log.error("2fa enabled and verification code is missing!", exception)
                response?.let {
                    writeResponse(
                        it,
                        ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                            .body<Any>(
                                ExceptionDTO(
                                    ExceptionWithCode("auth.missingVerificationCode", "Missing verification code!")
                                )
                            )
                    )
                }
            } else if (exception is InvalidTotpException) {
                log.error(
                    "2fa enabled and the totp verification token is incorrect!",
                    exception
                )
                response?.let {
                    writeResponse(
                        it,
                        ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                            .body<Any>(
                                ExceptionDTO(
                                    ExceptionWithCode(
                                        "auth.invalidVerificationCode",
                                        "The verification code is invalid!"
                                    )
                                )
                            )
                    )
                }
            } else if (exception.cause is HibernateException) {
                log.error(
                    "Hibernate exception while authenticating user: $username",
                    exception.cause
                )
                response?.let {
                    writeResponse(
                        it,
                        ResponseEntity.status(HttpStatus.UNAUTHORIZED).build<Any>()
                    )
                }
            } else {
                log.error("Invalid credentials for user: $username")
                response?.let {
                    writeResponse(
                        it,
                        ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                            .body<Any>(
                                ExceptionDTO(
                                    ExceptionWithCode("auth.invalidCredentials", "Invalid username/password!")
                                )
                            )
                    )
                }
            }
        }

    }

}

class JWTAuthorizationFilter(authManager: AuthenticationManager?, authenticatorService: AuthenticatorService) :
    BasicAuthenticationFilter(authManager) {
    private val authenticatorService: AuthenticatorService

    @Throws(IOException::class, ServletException::class)
    override fun doFilterInternal(
        req: HttpServletRequest,
        res: HttpServletResponse,
        chain: FilterChain
    ) {
        val token = req.getHeader(AUTHORIZATION_HEADER)
        if (token == null || !token.startsWith(TOKEN_PREFIX)) {
            chain.doFilter(req, res)
            return
        }
        val authentication: UsernamePasswordAuthenticationToken = authenticatorService.getAuthenticationOrFail(token)
        SecurityContextHolder.getContext().authentication = authentication
        chain.doFilter(req, res)
    }

    init {
        this.authenticatorService = authenticatorService
    }
}