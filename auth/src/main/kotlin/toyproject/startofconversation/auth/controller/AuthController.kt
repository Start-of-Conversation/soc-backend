package toyproject.startofconversation.auth.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.hibernate.annotations.Comment
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import toyproject.startofconversation.auth.controller.dto.AuthResponse
import toyproject.startofconversation.auth.controller.dto.OAuthParameter
import toyproject.startofconversation.auth.domain.entity.Auth
import toyproject.startofconversation.auth.domain.entity.value.AuthProvider
import toyproject.startofconversation.auth.service.AuthService
import toyproject.startofconversation.auth.service.SocialLoginService
import toyproject.startofconversation.common.base.dto.ResponseData
import toyproject.startofconversation.common.base.dto.ResponseInfo
import toyproject.startofconversation.common.exception.SOCDomainViolationException

@Tag(name = "Auth")
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
            ApiResponse(responseCode = "404", description = "유저를 찾을 수 없음")],
        security = [SecurityRequirement(name = "bearerAuth")]
    )
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping("/logout")
    fun logoutUser(request: HttpServletRequest, response: HttpServletResponse): ResponseEntity<ResponseInfo> =
        authService.logoutUser(request, response)

    @Comment("소셜 로그인 요청하기 위한 파라미터")
    @GetMapping("/params/{social}")
    fun getOauthParams(
        @Parameter(
            description = "소셜 로그인 제공자 (가능한 값: kakao, apple, naver)",
            example = "kakao",
            schema = Schema(implementation = AuthProvider::class)
        ) @PathVariable(required = true, name = "social") social: String
    ): OAuthParameter = socialLoginService.getOauthParams(AuthProvider.from(social))

    @Comment("소셜 로그인")
    @PostMapping("/{social}")
    fun loginSocialUser(
        @Parameter(
            description = "소셜 로그인 제공자 (가능한 값: kakao, apple, naver)",
            example = "kakao",
            schema = Schema(implementation = AuthProvider::class)
        ) @PathVariable("social") social: String,
        @Parameter(
            description = "로그인 후 받은 인가 코드 (authorizationCode and accessCode)",
            required = true
        ) @RequestParam("code") authorizationCode: String,
        @RequestParam(required = false, defaultValue = "state") state: String,
        response: HttpServletResponse
    ): ResponseEntity<ResponseData<AuthResponse>> =
        authService.loginUser(authorizationCode, state, response, AuthProvider.from(social))
}