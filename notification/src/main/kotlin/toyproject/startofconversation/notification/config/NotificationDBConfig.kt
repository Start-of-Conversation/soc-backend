package toyproject.startofconversation.notification.config

import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@Configuration
@EnableJpaRepositories(
    basePackages = ["toyproject.startofconversation.notification.device.domain.repository"]
)
@EntityScan(
    basePackages = [
        "toyproject.startofconversation.common.domain",
        "toyproject.startofconversation.log.notification.domain",
        "toyproject.startofconversation.log.system.domain",
        "toyproject.startofconversation.notification.device.domain"
    ]
)
@ComponentScan(
    basePackages = [
        "toyproject.startofconversation.common",
        "toyproject.startofconversation.log",
        "toyproject.startofconversation.notification"
    ]
)
class NotificationDBConfig