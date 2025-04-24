package toyproject.startofconversation.auth.kakao.dto


data class OAuthToken(
    val access_token: String,
    val token_type: String,
    val refresh_token: String,
    val expires_in: Long,
    val scope: String,
    val refresh_token_expires_in: Int
)

data class KakaoProfile(
    val id: Long,
    val connected_at: String,
    val properties: Properties,
    val kakao_account: KakaoAccount
)

data class Properties(val nickname: String?)

data class KakaoAccount(
    val email: String?,
    val is_email_verified: Boolean,
    val has_email: Boolean,
    val profile_nickname_needs_agreement: Boolean,
    val email_needs_agreement: Boolean,
    val is_email_valid: Boolean,
    val profile: Profile
)

data class Profile(
    val nickname: String?,
    val is_default_nickname: Boolean
)
