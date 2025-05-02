package toyproject.startofconversation.auth.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
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
import toyproject.startofconversation.common.exception.SOCForbiddenException

@RestController
@RequestMapping("/auth")
class AuthController(
    private val socialLoginService: SocialLoginService,
    private val authService: AuthService
) {

    @Operation(
        summary = "로그아웃",
        responses = [
            ApiResponse(responseCode = "200", description = "조회 성공"),
            ApiResponse(responseCode = "401", description = "인증 실패"),
            ApiResponse(responseCode = "403", description = "정지/탈퇴 유저"),
            ApiResponse(responseCode = "404", description = "유저를 찾을 수 없음")
        ],
        security = [SecurityRequirement(name = "bearerAuth")]
    )
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping("/logout")
    fun logoutUser(request: HttpServletRequest, response: HttpServletResponse): ResponseEntity<ResponseInfo> {
        val refreshToken = authService.deleteRefreshToken(request)
        response.addCookie(refreshToken)

        SecurityContextHolder.clearContext()

        val userId = SecurityUtil.getCurrentUserId()
        val headers = authService.generateExpiredAccessToken(userId)

        return ResponseEntity(ResponseInfo.to(Code.OK, "Logout successful"), headers, HttpStatus.OK)
    }

    @Comment("apple 소셜 로그인")
    @PostMapping("/apple")
    fun loginAppleUser(
        @RequestParam("code") authorizationCode: String,
        response: HttpServletResponse
    ): ResponseEntity<ResponseData<Auth>> = loginUser(authorizationCode, response, AuthProvider.APPLE)

    @Comment("kakao 소셜 로그인")
    @PostMapping("/kakao")
    fun loginKakaoUser(
        @RequestParam("code") accessCode: String,
        response: HttpServletResponse
    ): ResponseEntity<ResponseData<Auth>> = loginUser(accessCode, response, AuthProvider.KAKAO)

    @Comment("소셜 로그인 공통 로직")
    private fun loginUser(
        authorizationCode: String,
        response: HttpServletResponse,
        authProvider: AuthProvider
    ): ResponseEntity<ResponseData<Auth>> {
        // 소셜 로그인 처리
        val auth = socialLoginService.handleSocialLogin(authorizationCode, authProvider)

        if (auth.user.isDeleted) {
            throw SOCForbiddenException("User already deleted")
        }

        // 토큰 생성
        val headers = authService.generateToken(auth.user)

        // refreshToken 쿠키 생성
        val refreshTokenCookie = authService.generateRefreshToken(auth.user)

        // 쿠키 추가
        response.addCookie(refreshTokenCookie)

        // 성공 응답 반환
        return ResponseEntity(ResponseData.to(auth), headers, HttpStatus.OK)
    }
}