package toyproject.startofconversation.auth.naver.dto

data class NaverOauthTokenResponse(
    val accessToken: String,
    val tokenType: String,
    val refreshToken: String,
)