package toyproject.startofconversation.auth.apple.service

import io.jsonwebtoken.Jwts
import toyproject.startofconversation.auth.apple.dto.AppleOAuthProperties
import toyproject.startofconversation.auth.apple.dto.AppleTokenResponse
import toyproject.startofconversation.auth.apple.feign.AppleAuthTokenClient
import toyproject.startofconversation.auth.apple.security.AppleKeyLoader
import java.util.*

class AppleOAuthClientService(
    private val appleAuthTokenClient: AppleAuthTokenClient,
    private val appleOAuthProperties: AppleOAuthProperties,
    private val keyLoader: AppleKeyLoader
) {
    fun requestToken(code: String): AppleTokenResponse =
        appleAuthTokenClient.getAccessToken(
            code = code,
            clientId = appleOAuthProperties.aud,
            clientSecret = generateClientSecret(),
            redirectUri = appleOAuthProperties.redirectUri
        )

    private fun generateClientSecret(): String = Jwts.builder()
        .issuer(appleOAuthProperties.teamId)
        .setAudience(appleOAuthProperties.aud)
        .issuedAt(Date())
        .expiration(Date(System.currentTimeMillis() + 6 * 30L * 24 * 60 * 60 * 1000))
        .subject("appleAuth")
        .signWith(keyLoader.load(), Jwts.SIG.RS256)
        .compact()
}