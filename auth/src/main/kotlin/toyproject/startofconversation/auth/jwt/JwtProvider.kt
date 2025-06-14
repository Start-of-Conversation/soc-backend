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
import toyproject.startofconversation.common.exception.SOCUnauthorizedException
import toyproject.startofconversation.common.logger.logger
import java.util.*
import javax.crypto.SecretKey

@Component
class JwtProvider(
    @Value("\${jwt.secret-key}") private val secret: String,
    @Value("\${jwt.access-token-expire-time}") private val accessTokenExpireTime: Int,
    @Value("\${jwt.refresh-token-expire-time}") private val refreshTokenExpireTime: Int
) {

    private val log = logger()

    fun generateToken(user: Users, expirationTime: Int = accessTokenExpireTime): String {
        val claims = mutableMapOf<String, String>()

        val userId = user.id
        claims["userId"] = userId
        claims["role"] = "ROLE_${user.role.name}"

        val now = Date()
        val expiration = Date(now.time + expirationTime)
        val key = secretKey

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
            throw SOCUnauthorizedException("Invalid JWT signature", e)
        } catch (e: ExpiredJwtException) {
            log.info("만료된 JWT: {}", e.message)
            null
        } catch (e: Exception) {
            throw SOCUnauthorizedException("Invalid JWT token", e)
        }
    }

    fun generateRefreshToken(user: Users): String {
        val now = Date()
        val expiration = Date(now.time + refreshTokenExpireTime) // ex. 14일

        return Jwts.builder()
            .subject(user.id)
            .issuedAt(now)
            .expiration(expiration)
            .signWith(secretKey, Jwts.SIG.HS512)
            .compact()
    }

    private val secretKey: SecretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret))

}