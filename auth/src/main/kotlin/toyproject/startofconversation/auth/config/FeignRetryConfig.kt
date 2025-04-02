package toyproject.startofconversation.auth.config

import feign.Retryer
import feign.Retryer.Default
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class FeignRetryConfig {

    @Bean
    fun retryer() : Retryer = Default(1000, 1500, 1)
}