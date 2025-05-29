package toyproject.startofconversation.common.lock.strategy

import org.springframework.stereotype.Component

@Component
class NoOpLockService : LockService {
    override fun <T> executeWithLock(lockKey: String, timeout: Long, block: () -> T): T {
        return block() // 그냥 락 없이 실행
    }
}