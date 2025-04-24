package toyproject.startofconversation.auth.jwt

import io.jsonwebtoken.Claims
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import io.jsonwebtoken.security.SignatureException
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import toyproject.startofconversation.common.domain.user.entity.Users
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

    fun generateToken(user: Users, expirationTime: Int = accessTokenExpireTime): String {
        val claims = mutableMapOf<String, String>()

        val userId = user.getId()
        claims["userId"] = userId
        claims["email"] = user.email

        val now = Date()
        val expiration = Date(now.time + expirationTime)
        val key = Keys.hmacShaKeyFor(secret.toByteArray())

        return Jwts.builder()
            .claims(claims)
            .subject(userId)
            .issuedAt(now)
            .expiration(expiration)
            .signWith(key, Jwts.SIG.HS512)
            .compact()
    }

    fun generateExpiredToken(user: Users): String = generateToken(user, -1000)

    fun validateToken(token: String): Claims? {
        return try {
            Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .payload
        } catch (e: SignatureException) {
            throw SOCAuthException("Invalid JWT signature", e)
        } catch (e: ExpiredJwtException) {
            null
        } catch (e: Exception) {
            throw SOCAuthException("Invalid JWT token", e)
        }
    }

    fun generateRefreshToken(user: Users): String {
        val now = Date()
        val expiration = Date(now.time + refreshTokenExpireTime) // ex. 14일

        val key = Keys.hmacShaKeyFor(secret.toByteArray())

        return Jwts.builder()
            .subject(user.getId())
            .issuedAt(now)
            .expiration(expiration)
            .signWith(key, Jwts.SIG.HS512)
            .compact()
    }

}