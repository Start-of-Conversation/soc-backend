package toyproject.startofconversation.api.dto

import toyproject.startofconversation.common.domain.user.value.LoginChannel
import toyproject.startofconversation.common.domain.user.value.Role

data class RegisterUserRequest(
    val email: String,
    val password: String,
    val nickname: String,
    val profileImage: String?,
    val role: Role,
    val loginChannel: LoginChannel
)
