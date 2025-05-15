package toyproject.startofconversation.auth.apple.security

import org.springframework.stereotype.Component
import toyproject.startofconversation.auth.apple.dto.AppleOAuthProperties
import java.nio.file.Files
import java.nio.file.Paths
import java.security.KeyFactory
import java.security.interfaces.RSAPrivateKey
import java.security.spec.PKCS8EncodedKeySpec

@Component
class AppleKeyLoader(
    private val appleOAuthProperties: AppleOAuthProperties
) {

    fun load(): RSAPrivateKey {
        val keyBytes = Files.readAllBytes(Paths.get(appleOAuthProperties.key.path))
        val keySpec = PKCS8EncodedKeySpec(keyBytes)
        return KeyFactory.getInstance("RSA").generatePrivate(keySpec) as RSAPrivateKey
    }

}