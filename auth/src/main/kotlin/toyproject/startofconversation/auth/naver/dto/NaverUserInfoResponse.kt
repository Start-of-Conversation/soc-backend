package toyproject.startofconversation.auth.naver.dto

data class NaverUserInfoResponse(
    val resultcode: String,
    val message: String,
    val response: NaverUserDetail
)

data class NaverUserDetail(
    val id: String,
    val email: String?,
    val nickname: String?,
    val name: String?,
    val profile_image: String?,
    val age: String?,
    val gender: String?,
    val birthday: String?,
    val birthyear: String?,
    val mobile: String?
)