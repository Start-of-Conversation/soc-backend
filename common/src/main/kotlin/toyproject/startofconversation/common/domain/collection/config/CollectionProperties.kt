package toyproject.startofconversation.common.domain.collection.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "limits.collection")
class CollectionProperties {
    var maxCount: Int = 3
}