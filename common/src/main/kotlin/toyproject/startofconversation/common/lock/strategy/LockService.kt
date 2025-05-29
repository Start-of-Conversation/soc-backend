package toyproject.startofconversation.common.lock.strategy

/**
 * 이후 필요할 경우 Redisson 도입
 */
interface LockService {
    fun <T> executeWithLock(lockKey: String, timeout: Long = 3000L, block: () -> T): T
}