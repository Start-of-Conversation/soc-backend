package toyproject.startofconversation.api.image.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "path.file")
class ResourceProperties {
    lateinit var img: String
}