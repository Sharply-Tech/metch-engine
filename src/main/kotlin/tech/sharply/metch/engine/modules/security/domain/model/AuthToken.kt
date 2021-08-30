package tech.sharply.metch.engine.modules.security.domain.model

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.GrantedAuthority

class CustomUsernamePasswordAuthenticationToken : UsernamePasswordAuthenticationToken {
    //2fa code
    val verificationCode: String?

    @JvmOverloads
    constructor(principal: Any?, credentials: Any?, verificationCode: String? = null) : super(
        principal,
        credentials
    ) {
        this.verificationCode = verificationCode
    }

    constructor(
        principal: Any?, credentials: Any?, verificationCode: String?,
        authorities: Collection<GrantedAuthority?>?
    ) : super(principal, credentials, authorities) {
        this.verificationCode = verificationCode
    }

    constructor(
        principal: Any?, credentials: Any?,
        authorities: Collection<GrantedAuthority?>?
    ) : this(principal, credentials, null, authorities)

}
