package toyproject.startofconversation.auth.redis

import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Repository
import java.time.Duration

@Repository
class RedisRefreshTokenRepository(
    private val redisTemplate: RedisTemplate<String, String>
) {
    fun save(token: String, userId: String, ttl: Duration) {
        redisTemplate.opsForValue().set(token, userId, ttl)
    }

    fun findUserIdByToken(token: String): String? {
        return redisTemplate.opsForValue().get(token)
    }

    fun delete(token: String) {
        redisTemplate.delete(token)
    }
}