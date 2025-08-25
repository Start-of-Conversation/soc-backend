package toyproject.startofconversation.common.domain.cardgroup.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "limits.group.card")
class CardGroupProperties {
    var card: CardPropertySet = CardPropertySet()
    var custom: CustomPropertySet = CustomPropertySet()
}

class CardPropertySet {
    var maxCount: Int = 20
}

class CustomPropertySet {
    var maxCount: Int = 3
}