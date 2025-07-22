package toyproject.startofconversation.auth.apple.dto

import org.hibernate.annotations.Comment
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "social.apple")
class AppleOAuthProperties {

    @Comment("clientId")
    lateinit var aud: String
    lateinit var iss: String
    lateinit var teamId: String
    lateinit var redirectUri: String
    lateinit var auth: Auth
    lateinit var key: Key

    class Auth {
        lateinit var tokenUrl: String
        lateinit var publicKeyUrl: String
    }

    class Key {
        lateinit var id: String
        lateinit var path: String
    }
}