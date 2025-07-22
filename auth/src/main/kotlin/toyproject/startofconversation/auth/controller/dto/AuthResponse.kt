package toyproject.startofconversation.auth.controller.dto

import toyproject.startofconversation.auth.domain.entity.Auth
import toyproject.startofconversation.auth.domain.entity.value.AuthProvider
import java.time.LocalDateTime

data class AuthResponse(
    val email: String,
    val nickname: String,
    val authProvider: AuthProvider,
    val authId: String,
    val requiresPasswordUpdate: Boolean
) {
    companion object {
        fun from(auth: Auth): AuthResponse = with(auth) {
            AuthResponse(
                email = email,
                nickname = user.nickname,
                authProvider = authProvider,
                authId = id,
                requiresPasswordUpdate = needsPasswordUpdate(this)
            )
        }

        private fun needsPasswordUpdate(auth: Auth): Boolean = with(auth) {
            authProvider == AuthProvider.LOCAL &&
                lastPasswordModifiedAt.isBefore(LocalDateTime.now().minusDays(30))
        }
    }
}