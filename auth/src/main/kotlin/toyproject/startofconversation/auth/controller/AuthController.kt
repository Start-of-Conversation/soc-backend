package toyproject.startofconversation.auth.controller

import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import lombok.RequiredArgsConstructor
import org.hibernate.annotations.Comment
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import toyproject.startofconversation.auth.domain.entity.Auth
import toyproject.startofconversation.auth.domain.entity.value.AuthProvider
import toyproject.startofconversation.auth.service.AuthService
import toyproject.startofconversation.auth.service.SocialLoginService
import toyproject.startofconversation.auth.util.SecurityUtil
import toyproject.startofconversation.common.base.dto.ResponseData
import toyproject.startofconversation.common.base.dto.ResponseInfo
import toyproject.startofconversation.common.base.value.Code
import toyproject.startofconversation.common.exception.SOCAuthException
import toyproject.startofconversation.common.exception.SOCServerException

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
class AuthController(
    private val socialLoginService: SocialLoginService,
    private val authService: AuthService
) {

    @Comment("로그아웃 구현")
    @PostMapping("/logout")
    fun logoutUser(request: HttpServletRequest, response: HttpServletResponse): ResponseEntity<ResponseInfo> {
        try {
            // 쿠키에서 refreshToken을 삭제
            val refreshTokenCookie = Cookie("refreshToken", null)
            refreshTokenCookie.maxAge = 0
            refreshTokenCookie.path = "/"
            response.addCookie(refreshTokenCookie)

            SecurityContextHolder.clearContext()

            val userId = SecurityUtil.getCurrentUserId()
            val headers = authService.generateExpiredAccessToken(userId)
            authService.deleteRefreshToken(request)

            return ResponseEntity(ResponseInfo.to(Code.OK, "Logout successful"), headers, HttpStatus.OK)
        } catch (e: Exception) {
            throw SOCServerException("Error during logout")
        }
    }

    @Comment("apple 소셜 로그인 구현")
    @PostMapping("/apple")
    fun loginAppleUser(
        @RequestParam("code") authorizationCode: String,
        response: HttpServletResponse
    ): ResponseEntity<ResponseData<Auth>> = loginUser(authorizationCode, response, AuthProvider.APPLE)

    @Comment("kakao 소셜 로그인 구현")
    @PostMapping("/kakao")
    fun loginKakaoUser(
        @RequestParam("code") accessCode: String,
        response: HttpServletResponse
    ): ResponseEntity<ResponseData<Auth>> = loginUser(accessCode, response, AuthProvider.KAKAO)

    private fun loginUser(
        authorizationCode: String,
        response: HttpServletResponse,
        authProvider: AuthProvider
    ): ResponseEntity<ResponseData<Auth>> {
        try {
            // 소셜 로그인 처리
            val auth = socialLoginService.handleSocialLogin(authorizationCode, authProvider)

            // 토큰 생성
            val headers = authService.generateToken(auth.user)

            // refreshToken 쿠키 생성
            val refreshTokenCookie = authService.generateRefreshToken(auth.user)

            // 쿠키 추가
            response.addCookie(refreshTokenCookie)

            // 성공 응답 반환
            return ResponseEntity(ResponseData.to(auth), headers, HttpStatus.OK)
        } catch (e: Exception) {
            // 예외 처리
            throw SOCAuthException("Authentication failed")
        }
    }
}