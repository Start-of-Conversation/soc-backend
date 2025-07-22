package toyproject.startofconversation.common.domain.cardgroup.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "group.card")
class CardGroupProperties {
    var maxSize: Int = 20
}