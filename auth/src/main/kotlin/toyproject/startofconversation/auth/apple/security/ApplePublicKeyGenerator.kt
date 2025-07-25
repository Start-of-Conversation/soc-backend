package toyproject.startofconversation.auth.apple.security

import org.springframework.stereotype.Component
import toyproject.startofconversation.auth.apple.dto.ApplePublicKey
import toyproject.startofconversation.auth.apple.dto.ApplePublicKeyResponse
import java.io.IOException
import java.math.BigInteger
import java.security.InvalidKeyException
import java.security.KeyFactory
import java.security.KeyStoreException
import java.security.NoSuchAlgorithmException
import java.security.PublicKey
import java.security.spec.RSAPublicKeySpec
import java.util.Base64
import javax.naming.AuthenticationException

@Component
class ApplePublicKeyGenerator {

    @Throws(AuthenticationException::class, NoSuchAlgorithmException::class, InvalidKeyException::class)
    fun generatePublicKey(
        tokenHeaders: Map<String, String>,
        applePublicKeys: ApplePublicKeyResponse
    ): PublicKey = getPublicKey(
        applePublicKeys.getMatchKey(
            tokenHeaders.getValue("kid"),
            tokenHeaders.getValue("alg")
        )
    )

    @Throws(IOException::class, KeyStoreException::class, NoSuchAlgorithmException::class)
    private fun getPublicKey(publicKey: ApplePublicKey): PublicKey {
        val nBytes = Base64.getUrlDecoder().decode(publicKey.n)
        val eBytes = Base64.getUrlDecoder().decode(publicKey.e)

        val publicKeySpec = RSAPublicKeySpec(BigInteger(1, nBytes), BigInteger(1, eBytes))

        return KeyFactory.getInstance("RSA").generatePublic(publicKeySpec)
    }

}