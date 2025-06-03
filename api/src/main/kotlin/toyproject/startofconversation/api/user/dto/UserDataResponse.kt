package toyproject.startofconversation.api.user.dto

import toyproject.startofconversation.common.domain.user.entity.Users

data class UserDataResponse(
    val id: String,
    val nickname: String,
    val profileImageUrl: String,
) {
    companion object {
        fun to(user: Users): UserDataResponse = with(user) {
            UserDataResponse(
                id = getId(),
                nickname = nickname,
                profileImageUrl = profile,
            )
        }
    }
}