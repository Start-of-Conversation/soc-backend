package toyproject.startofconversation.notification.config

import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@Configuration
@EnableJpaRepositories(
    basePackages = ["toyproject.startofconversation.notification.domain.repository"]
)
@EntityScan(
    basePackages = [
        "toyproject.startofconversation.common.domain",
        "toyproject.startofconversation.notification.domain"
    ]
)
@ComponentScan(basePackages = ["toyproject.startofconversation.common"])
class NotificationDBConfig