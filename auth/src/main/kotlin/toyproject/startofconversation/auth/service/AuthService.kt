package toyproject.startofconversation.auth.service

import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletRequest
import jakarta.transaction.Transactional
import lombok.RequiredArgsConstructor
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpHeaders
import org.springframework.stereotype.Service
import toyproject.startofconversation.auth.domain.repository.AuthRepository
import toyproject.startofconversation.auth.jwt.JwtProvider
import toyproject.startofconversation.auth.redis.RedisRefreshTokenService
import toyproject.startofconversation.common.domain.user.entity.Users
import toyproject.startofconversation.common.domain.user.repository.UsersRepository
import toyproject.startofconversation.common.exception.SOCUnauthorizedException
import toyproject.startofconversation.common.exception.SOCNotFoundException
import toyproject.startofconversation.common.logger.logger

@Service
@RequiredArgsConstructor
class AuthService(
    private val jwtProvider: JwtProvider,
    private val usersRepository: UsersRepository,
    private val redisService: RedisRefreshTokenService,
    private val authRepository: AuthRepository,
) {
    private val KEY = "refreshToken"
    private val log = logger<AuthService>()

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

        val cookie = Cookie(KEY, refreshToken)
        cookie.isHttpOnly = true
        cookie.secure = true
        cookie.path = "/"
        cookie.maxAge = 60 * 60 * 24 * 14 // 14일

        return cookie
    }

    fun generateExpiredAccessToken(userId: String): HttpHeaders {
        val user = usersRepository.findByIdOrNull(userId) ?: throw SOCUnauthorizedException("No user found")
        val expiredToken = jwtProvider.generateExpiredToken(user)
        val responseHeaders = HttpHeaders().apply {
            set("Authorization", "Bearer $expiredToken") // 헤더에 토큰을 추가
        }

        return responseHeaders
    }

    fun deleteAuth(userId: String) = authRepository.deleteById(userId)

    @Transactional
    fun deleteRefreshToken(request: HttpServletRequest): Cookie {
        val refreshToken = getCookieValue(request, KEY)

        if (refreshToken.isNullOrBlank()) {
            log.warn("Refresh token not found in request cookies.")
        } else {
            redisService.revokeToken(refreshToken)
            log.info("Refresh token successfully revoked.")
        }

        val refreshTokenCookie = Cookie("refreshToken", null)
        refreshTokenCookie.maxAge = 0
        refreshTokenCookie.path = "/"

        return refreshTokenCookie
    }

    fun validateUserIdInToken(accessToken: String, userId: String): Boolean =
        jwtProvider.validateToken(accessToken)
            .takeIf { it != null }
            ?.let {
                if (it["userId"] == userId) return true
                else throw SOCUnauthorizedException("User ID mismatch: Token does not match provided userId")
            }
            ?: false

    fun validateRefreshTokenAndCreateAccessToken(refreshToken: String, userId: String): HttpHeaders {
        val user = usersRepository.findByIdOrNull(userId)
            ?: throw SOCNotFoundException("User ID mismatch: Token does not match provided userId")

        val userId = redisService.validateToken(refreshToken) ?: throw SOCUnauthorizedException("No cookie found")

        if (user.getId() != userId) throw SOCUnauthorizedException("User ID does not match provided userId")

        return generateToken(user)
    }

    fun getCookieValue(request: HttpServletRequest, cookieName: String): String? {
        val cookies = request.cookies
        cookies?.forEach {
            if (it.name == cookieName) {
                return it.value
            }
        }
        return null
    }

}