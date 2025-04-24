package toyproject.startofconversation.auth.apple.dto

data class AppleTokenResponse(
    val access_token: String,   // 애플에서 발급된 Access Token
    val id_token: String,       // 사용자 정보를 담고 있는 ID Token (JWT)
    val refresh_token: String,  // 리프레시 토큰 (새로 발급받기 위한 토큰)
    val expires_in: Int         // Access Token의 만료 시간 (초 단위)
)