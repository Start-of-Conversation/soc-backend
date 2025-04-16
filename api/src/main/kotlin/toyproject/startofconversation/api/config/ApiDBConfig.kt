package toyproject.startofconversation.api.config

import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@Configuration
@EnableJpaRepositories(basePackages = [
    "toyproject.startofconversation.common",
    "toyproject.startofconversation.auth.domain.repository"
])
@EntityScan(basePackages = [
    "toyproject.startofconversation.common",
    "toyproject.startofconversation.auth.domain.entity"
])
class ApiDBConfig {
}