package toyproject.startofconversation.auth.jwt.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@ConfigurationProperties(prefix = "jwt")
@Component
class JwtConfig {
    lateinit var secretKey: String
    var accessTokenExpireTime: Int = 0
    var refreshTokenExpireTime: Int = 0
}