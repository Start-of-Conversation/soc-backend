package toyproject.startofconversation.common.config

import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@Configuration
@EnableJpaAuditing
@EnableJpaRepositories(basePackages = ["toyproject.startofconversation.common"])
@EntityScan(basePackages = ["toyproject.startofconversation.common"])
@EnableConfigurationProperties
class CommonDBConfig