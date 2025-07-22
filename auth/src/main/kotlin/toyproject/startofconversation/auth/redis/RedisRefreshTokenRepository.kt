package toyproject.startofconversation.auth.redis

import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Repository
import java.time.Duration

@Repository
class RedisRefreshTokenRepository(
    private val redisTemplate: RedisTemplate<String, String>
) {
    fun save(key: String, value: String, ttl: Duration) =
        redisTemplate.opsForValue().set(key, value, ttl)

    fun findUserIdByToken(key: String): String? = redisTemplate.opsForValue().get(key)

    fun delete(token: String) = redisTemplate.delete(token)

}