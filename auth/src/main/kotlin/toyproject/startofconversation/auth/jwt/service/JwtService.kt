package toyproject.startofconversation.auth.jwt.service

import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletRequest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpHeaders
import org.springframework.stereotype.Service
import toyproject.startofconversation.auth.jwt.JwtProvider
import toyproject.startofconversation.auth.redis.RedisRefreshTokenService
import toyproject.startofconversation.common.domain.user.entity.Users
import toyproject.startofconversation.common.domain.user.exception.UserNotFoundException
import toyproject.startofconversation.common.domain.user.repository.UsersRepository
import toyproject.startofconversation.common.logger.logger

@Service
class JwtService(
    private val jwtProvider: JwtProvider,
    private val usersRepository: UsersRepository,
    private val redisService: RedisRefreshTokenService,
) {

    companion object {
        private const val REFRESH_TOKEN = "refreshToken"
    }

    private val log = logger()

    fun generateToken(user: Users): HttpHeaders {
        val accessToken = jwtProvider.generateToken(user)
        val responseHeaders = HttpHeaders().apply {
            set("Authorization", "Bearer $accessToken") // 헤더에 토큰을 추가
        }

        return responseHeaders
    }

    fun generateRefreshToken(user: Users): Cookie {
        val refreshToken = jwtProvider.generateRefreshToken(user)
        redisService.issueRefreshToken(refreshToken, user.id)

        val cookie = Cookie(REFRESH_TOKEN, refreshToken)
        cookie.isHttpOnly = true
        cookie.secure = false   // https일 경우 true로 설정
        cookie.path = "/"
        cookie.maxAge = 60 * 60 * 24 * 14 // 14일

        return cookie
    }

    fun generateExpiredAccessToken(userId: String): HttpHeaders {
        val user = usersRepository.findUserById(userId) ?: throw UserNotFoundException(userId)
        val expiredToken = jwtProvider.generateExpiredToken(user)
        val responseHeaders = HttpHeaders().apply {
            set("Authorization", "Bearer $expiredToken") // 헤더에 토큰을 추가
        }

        return responseHeaders
    }

    fun deleteRefreshToken(request: HttpServletRequest, userId: String): Cookie {
        val refreshToken = getCookieValue(request)

        if (refreshToken.isNullOrBlank()) {
            log.warn("Refresh token not found in request cookies.")
        } else {
            redisService.revokeToken(userId)
            log.info("Refresh token successfully revoked.")
        }

        val refreshTokenCookie = Cookie(REFRESH_TOKEN, null)
        refreshTokenCookie.maxAge = 0
        refreshTokenCookie.path = "/"

        return refreshTokenCookie
    }

    private fun getCookieValue(request: HttpServletRequest): String? =
        request.cookies?.firstOrNull { it.name == REFRESH_TOKEN }?.value


}