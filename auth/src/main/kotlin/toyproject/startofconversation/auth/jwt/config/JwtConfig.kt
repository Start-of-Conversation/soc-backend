package toyproject.startofconversation.auth.jwt.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "jwt")
class JwtConfig {
    var secretKey: String = ""
    var accessTokenExpireTime: Int = 0
    var refreshTokenExpireTime: Int = 0
}