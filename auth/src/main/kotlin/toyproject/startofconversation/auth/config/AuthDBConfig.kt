package toyproject.startofconversation.auth.config

import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import toyproject.startofconversation.auth.jwt.config.JwtConfig

@Configuration
@EnableJpaRepositories(
    basePackages = ["toyproject.startofconversation.auth.domain.repository"]
)
@EntityScan(
    basePackages = [
        "toyproject.startofconversation.common.domain",
        "toyproject.startofconversation.log.notification.domain",
        "toyproject.startofconversation.log.system.domain",
        "toyproject.startofconversation.notification.device.domain",
        "toyproject.startofconversation.auth.domain"
    ]
)
@ComponentScan(
    basePackages = [
        "toyproject.startofconversation.common",
        "toyproject.startofconversation.log",
        "toyproject.startofconversation.notification"
    ]
)
@EnableConfigurationProperties(JwtConfig::class)
class AuthDBConfig