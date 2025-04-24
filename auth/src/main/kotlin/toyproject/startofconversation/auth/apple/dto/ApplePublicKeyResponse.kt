package toyproject.startofconversation.auth.apple.dto

import toyproject.startofconversation.common.exception.SOCUnauthorizedException

data class ApplePublicKeyResponse(private val keys: List<ApplePublicKey>) {

    fun getMatchKey(kid: String, alg: String): ApplePublicKey =
        keys.find { it.kid == kid && it.alg == alg }
            ?: throw SOCUnauthorizedException("Apple public key not found")
}