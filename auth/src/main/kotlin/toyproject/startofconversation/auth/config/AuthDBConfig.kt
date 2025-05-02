package toyproject.startofconversation.auth.config

import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@Configuration
@EnableJpaRepositories(basePackages = ["toyproject.startofconversation.auth.domain.repository"])
@EntityScan(
    basePackages = [
        "toyproject.startofconversation.common.domain.user",
        "toyproject.startofconversation.auth"
    ]
)
class AuthDBConfig