package toyproject.startofconversation.common.config

import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@Configuration
@EnableJpaRepositories(basePackages = ["toyproject.startofconversation.common"])  // 레포지토리 위치
@EntityScan(basePackages = ["toyproject.startofconversation.common"])  // 엔티티 위치
@EnableConfigurationProperties
class CommonDBConfig {
}