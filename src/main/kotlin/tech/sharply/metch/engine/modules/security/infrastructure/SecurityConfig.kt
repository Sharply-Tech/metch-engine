package tech.sharply.metch.engine.modules.security.infrastructure

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.core.userdetails.UserDetailsService
import tech.sharply.metch.engine.modules.security.domain.model.LoginEventRepository
import tech.sharply.metch.engine.modules.security.domain.services.AuthenticatorService

@Configuration
@EnableWebSecurity
class SecurityConfig(
    @Autowired
    val userDetailsService: UserDetailsService,
    @Autowired
    val authenticatorService: AuthenticatorService,
    @Autowired
    val loginEventRepository: LoginEventRepository
) : WebSecurityConfigurerAdapter() {
}