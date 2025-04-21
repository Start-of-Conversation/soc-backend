package toyproject.startofconversation.auth.service

import jakarta.servlet.http.Cookie
import lombok.RequiredArgsConstructor
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpHeaders
import org.springframework.stereotype.Service
import toyproject.startofconversation.auth.jwt.JwtProvider
import toyproject.startofconversation.auth.redis.RedisRefreshTokenService
import toyproject.startofconversation.common.domain.user.entity.Users
import toyproject.startofconversation.common.domain.user.repository.UsersRepository
import toyproject.startofconversation.common.exception.SOCAuthException
import toyproject.startofconversation.common.exception.SOCNotFoundException

@Service
@RequiredArgsConstructor
class AuthService(
    private val jwtProvider: JwtProvider,
    private val usersRepository: UsersRepository,
    private val redisService: RedisRefreshTokenService
) {

    fun generateToken(user: Users): HttpHeaders {
        val accessToken = jwtProvider.generateToken(user)
        val responseHeaders = HttpHeaders().apply {
            set("Authorization", "Bearer $accessToken") // 헤더에 토큰을 추가
        }

        return responseHeaders
    }

    fun generateRefreshToken(user: Users): Cookie {
        val refreshToken = jwtProvider.generateRefreshToken(user)
        redisService.issueRefreshToken(refreshToken, user.getId())

        val cookie = Cookie("refreshToken", refreshToken)
        cookie.isHttpOnly = true
        cookie.secure = true
        cookie.path = "/"
        cookie.maxAge = 60 * 60 * 24 * 14 // 14일

        return cookie
    }

    fun validateUserIdInToken(accessToken: String, userId: String): Boolean =
        jwtProvider.validateToken(accessToken)
            .takeIf { it != null }
            ?.let {
                if (it["userId"] == userId) return true
                else throw SOCAuthException("User ID mismatch: Token does not match provided userId")
            }
            ?: false

    fun validateRefreshTokenAndCreateAccessToken(refreshToken: String, userId: String): HttpHeaders {
        val user = usersRepository.findByIdOrNull(userId)
            ?: throw SOCNotFoundException("User ID mismatch: Token does not match provided userId")

        val userId = redisService.validateToken(refreshToken) ?: throw SOCAuthException("No cookie found")

        if (user.getId() != userId) throw SOCAuthException("User ID does not match provided userId")

        return generateToken(user)
    }

}