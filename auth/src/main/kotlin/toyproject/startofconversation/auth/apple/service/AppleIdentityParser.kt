package toyproject.startofconversation.auth.apple.service

import org.springframework.stereotype.Component
import toyproject.startofconversation.auth.apple.security.ApplePublicKeyGenerator
import toyproject.startofconversation.auth.apple.feign.AppleAuthClient
import toyproject.startofconversation.auth.apple.provider.AppleJwtProvider
import java.security.PublicKey

@Component
class AppleIdentityParser(
    private val appleJwtProvider: AppleJwtProvider,
    private val applePublicKeyGenerator: ApplePublicKeyGenerator,
    private val appleAuthClient: AppleAuthClient
) {

    fun getAppleAccountId(identityToken: String): String {
        val headers: Map<String, String> = appleJwtProvider.parseHeaders(identityToken)
        val publicKey: PublicKey =
            applePublicKeyGenerator.generatePublicKey(headers, appleAuthClient.getAppleAuthPublicKey())
        return appleJwtProvider.getTokenClaims(identityToken, publicKey).subject
    }
}