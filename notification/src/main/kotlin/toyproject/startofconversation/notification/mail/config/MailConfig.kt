package toyproject.startofconversation.notification.mail.config

import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.JavaMailSenderImpl
import toyproject.startofconversation.notification.mail.config.properties.MailProperties
import java.util.Properties

@Configuration
@EnableConfigurationProperties(MailProperties::class)
class MailConfig(
    private val mailProperties: MailProperties
) {

    @Bean
    fun javaMailSender(): JavaMailSender {
        val mailSender = JavaMailSenderImpl()
        mailSender.host = mailProperties.host
        mailSender.port = mailProperties.port
        mailSender.username = mailProperties.username
        mailSender.password = mailProperties.password
        mailSender.javaMailProperties = getMailProperties()
        return mailSender
    }

    private fun getMailProperties(): Properties {
        val props = Properties()
        props["mail.transport.protocol"] = mailProperties.properties.transport.protocol
        props["mail.smtp.auth"] = mailProperties.properties.smtp.auth.toString()
        props["mail.smtp.ssl.enable"] = mailProperties.properties.smtp.ssl.enable.toString()
        props["mail.smtp.ssl.trust"] = mailProperties.host
        props["mail.debug"] = mailProperties.properties.debug
        return props
    }
}