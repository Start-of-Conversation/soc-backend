package toyproject.startofconversation.api.dto

import toyproject.startofconversation.common.domain.user.Users
import java.time.LocalDateTime

data class UserDataResponse(
    val id: String,
    val nickname: String,
    val profileImageUrl: String?,
    val acceptMarketing: Boolean,
    val acceptMarketingDate: LocalDateTime?
) {
    companion object {
        fun to(user: Users):UserDataResponse = UserDataResponse(
            id = user.getId(),
            nickname = user.nickname,
            profileImageUrl = user.profileImage,
            acceptMarketing = user.acceptMarketing,
            acceptMarketingDate = user.acceptMarketingDate
        )
    }
}
