package toyproject.startofconversation.api.config

import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@Configuration
@EnableJpaRepositories(basePackages = ["toyproject.startofconversation.common"])  // common 모듈의 레포지토리
@EntityScan(basePackages = ["toyproject.startofconversation.common"])  // common 모듈의 엔티티
class ApiDBConfig {
}