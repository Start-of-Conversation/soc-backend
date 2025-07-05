package toyproject.startofconversation.notification.fcm.config.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "fcm")
data class FCMProperties(
    var serviceAccountFile: String = "",
    var topicName: String = "",
    var projectId: String = ""
)