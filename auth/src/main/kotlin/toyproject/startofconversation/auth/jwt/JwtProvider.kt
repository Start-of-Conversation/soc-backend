package toyproject.startofconversation.auth.jwt

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import io.jsonwebtoken.security.SignatureException
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import toyproject.startofconversation.auth.domain.entity.Auth
import toyproject.startofconversation.common.exception.SOCAuthException
import java.util.*
import javax.crypto.SecretKey

@Component
class JwtProvider(
    @Value("\${jwt.secret-key}") private val secret: String,
    @Value("\${jwt.access-token-expire-time}") private val accessTokenExpireTime: Int,
    @Value("\${jwt.refresh-token-expire-time}") private val refreshTokenExpireTime: Int
) {
    private val secretKey: SecretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret))

    fun generateToken(auth: Auth): String {
        val claims = mutableMapOf<String, String>()
        claims["email"] = auth.user.email

        val now = Date()
        val expiration = Date(now.time + accessTokenExpireTime)
        val key = Keys.hmacShaKeyFor(secret.toByteArray())

        return Jwts.builder()
            .claims(claims)
            .subject(auth.authId)
            .issuedAt(now)
            .expiration(expiration)
            .signWith(key, Jwts.SIG.HS512)
            .compact()
    }

    fun validateToken(token: String): Claims {
        try {
            return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .payload
        } catch (e: SignatureException) {
            throw SOCAuthException.of("Invalid JWT signature", e)
        } catch (e: Exception) {
            throw SOCAuthException.of("Invalid JWT token", e)
        }
    }
}