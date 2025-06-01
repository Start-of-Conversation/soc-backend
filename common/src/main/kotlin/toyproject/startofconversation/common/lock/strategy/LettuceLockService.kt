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
        val value = Thread.currentThread().name + "-" + System.currentTimeMillis()
        val timeout = lockProperties.timeoutMillis

        val lockAcquired = redisTemplate.opsForValue()
            .setIfAbsent(lockKey, value, timeout, TimeUnit.MILLISECONDS)

        if (lockAcquired == true) {
            try {
                return block()
            } finally {
                redisTemplate.delete(lockKey)
            }
        } else {
            throw LockAcquisitionException(lockKey, timeout)
        }
    }
}