package toyproject.startofconversation.common.lock.strategy

import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Component
import toyproject.startofconversation.common.lock.config.LockProperties
import toyproject.startofconversation.common.lock.exception.LockAcquisitionException
import java.util.concurrent.TimeUnit

/**
 * TODO 비즈니스가 커질 시 Redisson으로 변경할 것
 */
@Component
class LettuceLockService(
    private val redisTemplate: RedisTemplate<String, String>,
    private val lockProperties: LockProperties
) : LockService {

    override fun <T> executeWithLock(lockKey: String, block: () -> T): T {
        val value = "${Thread.currentThread().name}-${System.currentTimeMillis()}"
        val timeout = lockProperties.timeoutMillis
        val retryInterval = lockProperties.retryIntervalMillis
        val maxRetries = lockProperties.maxRetries

        var retries = 0
        while (retries < maxRetries) {
            val lockAcquired = redisTemplate.opsForValue()
                .setIfAbsent(lockKey, value, timeout, TimeUnit.MILLISECONDS)

            if (lockAcquired == true) {
                try {
                    return block()
                } finally {
                    // 안전한 락 해제 (value 비교 후 삭제)
                    if (redisTemplate.opsForValue().get(lockKey) == value) {
                        redisTemplate.delete(lockKey)
                    }
                }
            }

            retries++
            Thread.sleep(retryInterval)
        }

        throw LockAcquisitionException(lockKey, timeout)
    }
}