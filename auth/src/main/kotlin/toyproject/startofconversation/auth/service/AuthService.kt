package toyproject.startofconversation.auth.service

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import jakarta.transaction.Transactional
import org.hibernate.annotations.Comment
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import toyproject.startofconversation.auth.domain.entity.Auth
import toyproject.startofconversation.auth.domain.entity.value.AuthProvider
import toyproject.startofconversation.auth.domain.repository.AuthRepository
import toyproject.startofconversation.auth.jwt.service.JwtService
import toyproject.startofconversation.auth.util.SecurityUtil
import toyproject.startofconversation.common.base.dto.ResponseData
import toyproject.startofconversation.common.base.dto.ResponseInfo
import toyproject.startofconversation.common.base.value.Code
import toyproject.startofconversation.common.exception.SOCForbiddenException
import toyproject.startofconversation.common.logger.logger

@Service
class AuthService(
    private val authRepository: AuthRepository,
    private val socialLoginService: SocialLoginService,
    private val jwtService: JwtService
) {
    private val log = logger<AuthService>()

    fun deleteAuth(userId: String) = authRepository.deleteById(userId)

    @Comment("소셜 로그인 공통 로직")
    fun loginUser(
        authorizationCode: String,
        state: String,
        response: HttpServletResponse,
        authProvider: AuthProvider
    ): ResponseEntity<ResponseData<Auth>> {
        // 소셜 로그인 처리
        val auth = socialLoginService.handleSocialLogin(authorizationCode, state, authProvider)

        if (auth.user.isDeleted) {
            throw SOCForbiddenException("User already deleted")
        }

        // 토큰 생성
        val headers = jwtService.generateToken(auth.user)

        // refreshToken 쿠키 생성
        val refreshTokenCookie = jwtService.generateRefreshToken(auth.user)

        // 쿠키 추가
        response.addCookie(refreshTokenCookie)

        // 성공 응답 반환
        return ResponseEntity(ResponseData.to(auth), headers, HttpStatus.OK)
    }

    @Transactional
    fun logoutUser(request: HttpServletRequest, response: HttpServletResponse): ResponseEntity<ResponseInfo> {
        val refreshToken = jwtService.deleteRefreshToken(request)
        response.addCookie(refreshToken)

        SecurityContextHolder.clearContext()

        val userId = SecurityUtil.getCurrentUserId()
        val headers = jwtService.generateExpiredAccessToken(userId)

        return ResponseEntity(ResponseInfo.to(Code.OK, "Logout successful"), headers, HttpStatus.OK)
    }
}