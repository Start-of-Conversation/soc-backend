package toyproject.startofconversation.api.admin.dto

import toyproject.startofconversation.common.domain.user.entity.value.Role
import java.time.LocalDateTime

data class AdminUserListResponse(
    val id: String,
    val nickname: String,
    val role: Role,
    val registrationDate: LocalDateTime?,
    val isWithdrawn: Boolean,
    val withdrawalDate: LocalDateTime?,
    val auth: Set<AdminUserDto>
)