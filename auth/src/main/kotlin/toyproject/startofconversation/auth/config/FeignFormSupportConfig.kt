package toyproject.startofconversation.auth.config

import feign.Retryer
import feign.Retryer.Default
import feign.codec.Encoder
import feign.form.FormEncoder
import feign.form.spring.SpringFormEncoder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class FeignFormSupportConfig {

    @Bean
    fun feignFormEncoder(): Encoder = FormEncoder(SpringFormEncoder())

    @Bean
    fun retryer(): Retryer = Default(1000, 1500, 1)
}