package tech.sharply.metch.engine.modules.security.application.dto

import java.util.*

class LoginRequest(
    val username: String,
    val password: String,
    val verificationCode: String
)

class LoginResponse(
    val token: String,
    val expiresAt: Date
)