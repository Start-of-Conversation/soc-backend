package toyproject.startofconversation.auth.apple.dto

open class ApplePublicKey(
    val kty: String,
    val kid: String,
    val alg: String,
    val n: String,
    val e: String
)