package toyproject.startofconversation.auth.controller.dto

import toyproject.startofconversation.auth.domain.entity.Auth
import toyproject.startofconversation.auth.domain.entity.value.AuthProvider

data class AuthResponse(
    val email: String,
    val nickname: String,
    val authProvider: AuthProvider,
    val authId: String
) {
    companion object {
        fun from(auth: Auth): AuthResponse =
            AuthResponse(
                email = auth.email,
                nickname = auth.user.nickname,
                authProvider = auth.authProvider,
                authId = auth.getId()
            )
    }
}