package toyproject.startofconversation.auth.naver.dto

data class NaverTokenRequest(
    val grant_type: String = "authorization_code",
    val client_id: String,
    val client_secret: String,
    val code: String,
    val state: String
)