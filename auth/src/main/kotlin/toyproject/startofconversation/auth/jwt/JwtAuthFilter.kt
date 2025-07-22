package toyproject.startofconversation.auth.jwt

import io.jsonwebtoken.Claims
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import toyproject.startofconversation.auth.redis.RedisRefreshTokenRepository
import toyproject.startofconversation.common.domain.user.repository.UsersRepository
import toyproject.startofconversation.common.exception.SOCNotFoundException
import toyproject.startofconversation.common.exception.SOCUnauthorizedException
import toyproject.startofconversation.common.logger.logger

@Component
class JwtAuthFilter(
    private val jwtProvider: JwtProvider,
    private val refreshTokenRepository: RedisRefreshTokenRepository,
    private val usersRepository: UsersRepository
) : OncePerRequestFilter() {

    private val log = logger()

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val requestURI = request.requestURI
        if (!requestURI.startsWith("/health")) {
            log.info("요청 methde: ${request.method} api: $requestURI")
        }

        // 화이트리스트 경로는 인증 없이 통과
        if (isPublicApi(requestURI) || isSwagger(requestURI)) {
            filterChain.doFilter(request, response)
            return
        }

        val accessToken = resolveAccessToken(request)
        val refreshToken = resolveRefreshToken(request)

        /*
                request.cookies?.forEach {
                    log.debug("쿠키 확인 - 이름: ${it.name}, 값: ${it.value}")
                }
                log.debug("추출된 refreshToken: {}", refreshToken)

                log.debug("요청 URI: {}", requestURI)
                log.debug("액세스 토큰: {}", accessToken)
        */

        try {
            // ✅ Access Token이 유효한 경우
            val claims = accessToken?.let { jwtProvider.validateToken(it) }
            if (claims != null) {
                setAuthentication(claims.subject, claims)
                filterChain.doFilter(request, response)
                return
            }

            // ✅ Access Token 만료, Refresh Token이 있는 경우
            if (!refreshToken.isNullOrBlank()) {
                val refreshClaims = jwtProvider.validateToken(refreshToken)
                if (refreshClaims != null) {
                    val userId = refreshTokenRepository.findUserIdByToken(refreshToken)
                        ?: throw SOCUnauthorizedException("Invalid refresh token")
                    val user = usersRepository.findByIdOrNull(userId)
                        ?: throw SOCNotFoundException("User not found")

                    val newAccessToken = jwtProvider.generateToken(user)
                    response.setHeader("Authorization", "Bearer $newAccessToken")

                    setAuthentication(userId, refreshClaims)
                    filterChain.doFilter(request, response)
                    return
                }
            }

            // ✅ Access, Refresh 모두 유효하지 않으면 예외
            throw SOCUnauthorizedException("Unauthorized")

        } catch (e: Exception) {
            // ❗ 응답을 확실히 마무리
            log.warn("인증 실패: {}", e.message)
            response.status = HttpServletResponse.SC_UNAUTHORIZED
            response.contentType = "application/json;charset=UTF-8"
            response.writer.write("{\"error\": \"Authentication failed: ${e.message}\"}")
            response.writer.flush()
            response.writer.close()
        }
    }

    private fun isPublicApi(requestURI: String): Boolean =
        (requestURI.startsWith("/auth") && requestURI != "/auth/logout" && requestURI != "/auth/local/password")
            || requestURI.matches(Regex("/.*/public(/.*)?"))
            || requestURI.startsWith("/favicon.ico")
            || requestURI.startsWith("/health")

    private fun isSwagger(requestURI: String): Boolean =
        requestURI.startsWith("/swagger-ui")
            || requestURI.startsWith("/v3/api-docs")
            || requestURI.startsWith("/swagger-resources")
            || requestURI.startsWith("/favicon.ico")

    private fun resolveAccessToken(request: HttpServletRequest): String? {
        val header = request.getHeader("Authorization")
        return if (!header.isNullOrBlank() && header.startsWith("Bearer ")) {
            val substring = header.substring(7)
            substring
        } else null
    }

    private fun resolveRefreshToken(request: HttpServletRequest): String? {
        return request.cookies?.firstOrNull { it.name == "refreshToken" }?.value
    }

    private fun setAuthentication(userId: String, claims: Claims) {
        val role = claims["role"] as String
        val auth = UsernamePasswordAuthenticationToken(userId, null, listOf(SimpleGrantedAuthority(role)))
        SecurityContextHolder.getContext().authentication = auth

        log.info("✅ 인증 객체: {}, 인증 여부: {}", auth, auth.isAuthenticated)
    }
}