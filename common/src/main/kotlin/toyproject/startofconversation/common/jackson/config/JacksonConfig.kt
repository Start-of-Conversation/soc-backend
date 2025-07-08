package toyproject.startofconversation.common.jackson.config

import com.fasterxml.jackson.databind.module.SimpleModule
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import toyproject.startofconversation.common.jackson.LocalDateTimeSerializer
import java.time.LocalDateTime

@Configuration
class JacksonConfig {

    @Bean
    fun localDateTimeModule(): SimpleModule = SimpleModule().apply {
        addSerializer(LocalDateTime::class.java, LocalDateTimeSerializer())
    }
}