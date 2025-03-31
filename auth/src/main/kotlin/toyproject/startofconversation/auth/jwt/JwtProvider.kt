package toyproject.startofconversation.auth.jwt

import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import javax.crypto.SecretKey

@Component
class JwtProvider(
    @Value("\${jwt.secret-key}") private val secret: String,

    @Value("\${jwt.access-token-expire-time}") private val accessTokenExpireTime: String,

    @Value("\${jwt.refresh-token-expire-time}") private val refreshTokenExpireTime: String
) {
    private val secretKey: SecretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret))

}