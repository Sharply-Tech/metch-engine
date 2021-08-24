package tech.sharply.metch.engine.modules.security.infrastructure.constants

class SecurityConstants {

    companion object {
        const val SECRET = "FPztyNiMmvMB3PyP2qqKh7mh6cZS7DQL"
        const val EXPIRATION_TIME: Long = 86400000 // 1 day
        const val TOKEN_PREFIX = "Bearer "
        const val AUTHORIZATION_HEADER = "Authorization"
    }

}