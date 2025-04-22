package toyproject.startofconversation.auth.jwt

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import toyproject.startofconversation.auth.redis.RedisRefreshTokenRepository
import toyproject.startofconversation.common.domain.user.repository.UsersRepository
import toyproject.startofconversation.common.exception.SOCNotFoundException

@Component
class JwtAuthFilter(
    private val jwtProvider: JwtProvider,
    private val refreshTokenRepository: RedisRefreshTokenRepository,
    private val usersRepository: UsersRepository
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val path = request.requestURI

        //whiteList에 있는 경로의 경우 인증 없이 패스
        if (path.startsWith("/auth") || path.matches(Regex("/api/.*/public/.*"))) {
            filterChain.doFilter(request, response)
            return
        }

        val accessToken = resolveAccessToken(request)
        val refreshToken = resolveRefreshToken(request)

        try {
            val claims = accessToken?.let { jwtProvider.validateToken(it) }

            if (claims != null) {
                // 유효한 accessToken
                val userId = claims.subject
                setAuthentication(userId)
                filterChain.doFilter(request, response)
                return
            }

            // accessToken이 만료되었고, refreshToken이 존재할 때
            if (!refreshToken.isNullOrBlank()) {
                val refreshClaims = jwtProvider.validateToken(refreshToken)

                if (refreshClaims != null) {
                    val userId = refreshTokenRepository.findUserIdByToken(refreshToken)
                        ?: throw RuntimeException("Invalid refresh token")

                    val user = usersRepository.findUsersById(userId) ?: throw SOCNotFoundException("User not found")
                    val newAccessToken = jwtProvider.generateToken(user)

                    response.setHeader("Authorization", "Bearer $newAccessToken")

                    // SecurityContext 설정 후 통과
                    setAuthentication(userId)
                    filterChain.doFilter(request, response)
                    return
                }
            }

            throw RuntimeException("Unauthorized")

        } catch (e: Exception) {
            response.status = HttpServletResponse.SC_UNAUTHORIZED
            response.writer.write("Authentication failed: ${e.message}")
        }
    }

    private fun resolveAccessToken(request: HttpServletRequest): String? {
        val header = request.getHeader("Authorization")
        return if (!header.isNullOrBlank() && header.startsWith("Bearer ")) {
            header.substring(7)
        } else null
    }

    private fun resolveRefreshToken(request: HttpServletRequest): String? {
        return request.cookies?.firstOrNull { it.name == "refreshToken" }?.value
    }

    private fun setAuthentication(userId: String) {
        val auth = UsernamePasswordAuthenticationToken(userId, null, emptyList())
        SecurityContextHolder.getContext().authentication = auth
    }
}