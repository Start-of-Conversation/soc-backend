package toyproject.startofconversation.common.lock.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "lock")
class LockProperties {
    var enabled: Boolean = true
    var timeoutMillis: Long = 3000
}