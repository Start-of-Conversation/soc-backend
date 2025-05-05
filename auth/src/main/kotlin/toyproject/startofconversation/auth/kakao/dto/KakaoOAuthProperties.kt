package toyproject.startofconversation.auth.kakao.dto

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "social.kakao")
class KakaoOAuthProperties {

    lateinit var clientId: String
    lateinit var redirectUri: String

}