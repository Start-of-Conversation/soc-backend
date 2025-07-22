package toyproject.startofconversation.notification.mail.config.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "spring.mail")
class MailProperties {
    var host: String = ""
    var port: Int = 0
    var username: String = ""
    var password: String = ""
    var properties: MailPropertySet = MailPropertySet()
}

class MailPropertySet {
    var transport: TransportProperties = TransportProperties()
    var smtp: SMTPProperties = SMTPProperties()
    var debug: Boolean = false
}

class TransportProperties {
    var protocol: String = ""
}

class SMTPProperties {
    var auth: Boolean = true
    var ssl: SSLProperties = SSLProperties()
    var starttls: StartTLSProperties = StartTLSProperties()
}

class SSLProperties {
    var enable: Boolean = true
    var trust: String = ""
}

class StartTLSProperties {
    var enable: Boolean = true
}