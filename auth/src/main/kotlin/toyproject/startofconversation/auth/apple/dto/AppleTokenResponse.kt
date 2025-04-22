package toyproject.startofconversation.auth.apple.dto

data class AppleTokenResponse(
    val accessToken: String,   // 애플에서 발급된 Access Token
    val idToken: String,       // 사용자 정보를 담고 있는 ID Token (JWT)
    val refreshToken: String,  // 리프레시 토큰 (새로 발급받기 위한 토큰)
    val expiresIn: Int         // Access Token의 만료 시간 (초 단위)
)