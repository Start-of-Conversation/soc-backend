package toyproject.startofconversation.api.config

import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration

@Configuration
@EntityScan(
    basePackages = [
        "toyproject.startofconversation.common.domain",
        "toyproject.startofconversation.auth.domain"
    ]
)
@ComponentScan(
    basePackages = [
        "toyproject.startofconversation.common",
        "toyproject.startofconversation.auth",
        "toyproject.startofconversation.api"
    ]
)
class ApiDBConfig