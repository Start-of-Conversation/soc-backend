package toyproject.startofconversation.auth.naver.dto

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component


@Component
@ConfigurationProperties(prefix = "social.naver")
class NaverOAuthProperties {
    lateinit var params: Params
    lateinit var path: Path

    class Params {
        lateinit var clientId: String
        lateinit var clientSecret: String
    }

    class Path {
        lateinit var redirectUri: String
        lateinit var tokenUrl: String
        lateinit var userInfoUrl: String
    }
}