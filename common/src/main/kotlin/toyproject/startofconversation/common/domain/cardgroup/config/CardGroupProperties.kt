package toyproject.startofconversation.common.domain.cardgroup.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "limits.group.card")
class CardGroupProperties {
    var maxCount: Int = 20
}