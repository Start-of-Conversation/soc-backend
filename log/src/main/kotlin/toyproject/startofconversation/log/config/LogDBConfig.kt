package toyproject.startofconversation.log.config

import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@Configuration
@EnableJpaRepositories(
    basePackages = [
        "toyproject.startofconversation.log.notification.domain.repository",
        "toyproject.startofconversation.log.system.domain.repository"
    ]
)
@EntityScan(
    basePackages = [
        "toyproject.startofconversation.common.domain",
        "toyproject.startofconversation.log.notification.domain",
        "toyproject.startofconversation.log.system.domain"
    ]
)
@ComponentScan(basePackages = ["toyproject.startofconversation.common"])
class LogDBConfig {
}