package toyproject.startofconversation.auth.apple

import lombok.RequiredArgsConstructor
import org.springframework.stereotype.Component
import toyproject.startofconversation.auth.apple.dto.ApplePublicKey
import toyproject.startofconversation.auth.apple.dto.ApplePublicKeyResponse
import java.math.BigInteger
import java.security.KeyFactory
import java.security.PublicKey
import java.security.spec.RSAPublicKeySpec
import java.util.*

@Component
@RequiredArgsConstructor
class ApplePublicKeyGenerator {

    fun generatePublicKey(
        tokenHeaders: Map<String, String>,
        applePublicKeys: ApplePublicKeyResponse
    ): PublicKey = getPublicKey(
        applePublicKeys.getMatchKey(
            tokenHeaders.getValue("kid"),
            tokenHeaders.getValue("alg")
        )
    )

    private fun getPublicKey(publicKey: ApplePublicKey): PublicKey {
        val nBytes = Base64.getUrlDecoder().decode(publicKey.n)
        val eBytes = Base64.getUrlDecoder().decode(publicKey.e)

        val publicKeySpec = RSAPublicKeySpec(BigInteger(1, nBytes), BigInteger(1, eBytes))

        return KeyFactory.getInstance("RSA").generatePublic(publicKeySpec)
    }


}