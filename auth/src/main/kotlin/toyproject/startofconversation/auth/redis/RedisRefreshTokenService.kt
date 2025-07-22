package toyproject.startofconversation.auth.redis

import org.springframework.stereotype.Service
import java.time.Duration

@Service
class RedisRefreshTokenService(
    private val repository: RedisRefreshTokenRepository
) {
    fun issueRefreshToken(token: String, userId: String) = repository.save(
        key = makeKey(userId), value = token, ttl = ttl
    )

    fun validateToken(userId: String, token: String): Boolean =
        repository.findUserIdByToken(key = makeKey(userId))?.let { it == token } ?: false

    fun revokeToken(userId: String) = repository.delete(makeKey(userId))


    private fun makeKey(userId: String): String = "refresh:user_$userId"

    companion object {
        private val ttl : Duration = Duration.ofDays(14)
    }
}