package toyproject.startofconversation.auth.redis

import java.time.Duration

class RedisRefreshTokenService(
    private val repository: RedisRefreshTokenRepository
) {
    fun issueRefreshToken(token: String, userId: String) {
        val ttl = Duration.ofDays(14)
        repository.save(token, userId, ttl)
    }

    fun validateToken(token: String): String? {
        return repository.findUserIdByToken(token)
    }

    fun revokeToken(token: String) {
        repository.delete(token)
    }
}