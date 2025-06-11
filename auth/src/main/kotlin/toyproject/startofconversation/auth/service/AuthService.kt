package toyproject.startofconversation.auth.service

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.hibernate.annotations.Comment
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import toyproject.startofconversation.auth.controller.dto.AuthResponse
import toyproject.startofconversation.auth.controller.dto.LocalLoginRequest
import toyproject.startofconversation.auth.domain.entity.Auth
import toyproject.startofconversation.auth.domain.entity.value.AuthProvider
import toyproject.startofconversation.auth.domain.repository.AuthRepository
import toyproject.startofconversation.auth.jwt.service.JwtService
import toyproject.startofconversation.auth.local.service.LocalAuthService
import toyproject.startofconversation.auth.util.SecurityUtil
import toyproject.startofconversation.common.base.dto.ResponseData
import toyproject.startofconversation.common.base.dto.ResponseInfo
import toyproject.startofconversation.common.base.value.Code
import toyproject.startofconversation.common.logger.logger
import toyproject.startofconversation.common.transaction.helper.Tx

@Service
class AuthService(
    private val authRepository: AuthRepository,
    private val socialLoginService: SocialLoginService,
    private val localAuthService: LocalAuthService,
    private val jwtService: JwtService
) {
    private val log = logger<AuthService>()

    fun deleteAuth(userId: String) = Tx.writeTx {
        if (authRepository.existsById(userId)) {
            authRepository.deleteAllByUserId(userId)
        }
    }

    @Comment("소셜 로그인 공통 로직")
    fun loginUser(
        authorizationCode: String,
        state: String,
        response: HttpServletResponse,
        authProvider: AuthProvider
    ): ResponseEntity<ResponseData<AuthResponse>> =
        processLogin(socialLoginService.handleSocialLogin(authorizationCode, state, authProvider), response)

    @Comment("로컬 로그인 로직")
    fun loginLocalUser(
        request: LocalLoginRequest,
        response: HttpServletResponse
    ): ResponseEntity<ResponseData<AuthResponse>> = processLogin(localAuthService.findUser(request), response)

    fun logoutUser(request: HttpServletRequest, response: HttpServletResponse): ResponseEntity<ResponseInfo> {
        val refreshToken = jwtService.deleteRefreshToken(request)
        response.addCookie(refreshToken)

        SecurityContextHolder.clearContext()

        val userId = SecurityUtil.getCurrentUserId()
        val headers = jwtService.generateExpiredAccessToken(userId)

        return ResponseEntity(ResponseInfo.to(Code.OK, "Logout successful"), headers, HttpStatus.OK)
    }

    private fun processLogin(
        auth: Auth,
        response: HttpServletResponse
    ): ResponseEntity<ResponseData<AuthResponse>> {
        val user = auth.activeUser

        val headers = jwtService.generateToken(user)
        val refreshTokenCookie = jwtService.generateRefreshToken(user)
        response.addCookie(refreshTokenCookie)

        return ResponseEntity(ResponseData.to(AuthResponse.from(auth)), headers, HttpStatus.OK)
    }
}