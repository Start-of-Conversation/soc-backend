package toyproject.startofconversation.api.admin.dto

import toyproject.startofconversation.auth.domain.entity.value.AuthProvider
import java.time.LocalDateTime

data class AdminUserDto(
    val id: String,
    val email: String,
    val channel: AuthProvider,
    val createDate: LocalDateTime,
)