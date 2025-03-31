package toyproject.startofconversation.auth.apple.dto

data class ApplePublicKey(
    val kty: String,
    val kid: String,
    val alg: String,
    val n: String,
    val e: String
)