package tech.sharply.metch.engine.modules.security.domain.exceptions

import org.springframework.security.core.AuthenticationException

class InvalidTotpException : AuthenticationException {
    constructor(explanation: String?) : super(explanation)
    constructor(msg: String?, t: Throwable?) : super(msg, t)
}

class MissingTotpException : AuthenticationException {
    constructor(msg: String?, t: Throwable?) : super(msg, t)
    constructor(msg: String?) : super(msg)
}
