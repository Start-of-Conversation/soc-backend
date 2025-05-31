package toyproject.startofconversation.common.lock.config

import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.core.RedisTemplate
import toyproject.startofconversation.common.lock.strategy.LettuceLockService
import toyproject.startofconversation.common.lock.strategy.LockService
import toyproject.startofconversation.common.lock.strategy.NoOpLockService

@Configuration
@EnableConfigurationProperties(LockProperties::class)
class LockConfig {
    @Bean
    fun lockService(
        redisTemplate: RedisTemplate<String, String>,
        lockProperties: LockProperties
    ): LockService {
        return if (lockProperties.enabled) {
            LettuceLockService(redisTemplate, lockProperties)
        } else {
            NoOpLockService()
        }
    }
}