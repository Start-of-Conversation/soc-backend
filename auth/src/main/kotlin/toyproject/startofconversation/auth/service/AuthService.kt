package toyproject.startofconversation.auth.service

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import toyproject.startofconversation.auth.controller.dto.AuthResponse
import toyproject.startofconversation.auth.controller.dto.LocalLoginRequest
import toyproject.startofconversation.auth.controller.dto.LocalRegisterRequest
import toyproject.startofconversation.auth.controller.dto.PasswordUpdateRequest
import toyproject.startofconversation.auth.domain.entity.Auth
import toyproject.startofconversation.auth.domain.entity.value.AuthProvider
import toyproject.startofconversation.auth.domain.repository.AuthRepository
import toyproject.startofconversation.auth.jwt.service.JwtService
import toyproject.startofconversation.auth.local.service.LocalAuthService
import toyproject.startofconversation.common.base.dto.ResponseData
import toyproject.startofconversation.common.base.dto.ResponseInfo
import toyproject.startofconversation.common.base.value.Code
import toyproject.startofconversation.common.logger.logger
import toyproject.startofconversation.common.transaction.helper.Tx
import toyproject.startofconversation.notification.mail.MailService

@Service
class AuthService(
    private val authRepository: AuthRepository,
    private val socialLoginService: SocialLoginService,
    private val localAuthService: LocalAuthService,
    private val jwtService: JwtService,
    private val mailService: MailService
) {
    private val log = logger<AuthService>()

    fun deleteAuth(userId: String) = Tx.writeTx {
        if (authRepository.existsByUserId(userId)) {
            authRepository.deleteAllByUserId(userId)
        }
    }

    /**
     * 소셜 로그인 공통 로직
     */
    fun loginUser(
        authorizationCode: String,
        state: String,
        response: HttpServletResponse,
        authProvider: AuthProvider
    ): ResponseEntity<ResponseData<AuthResponse>> =
        processLogin(socialLoginService.handleSocialLogin(authorizationCode, state, authProvider), response)

    /**
     * 로컬 로그인 로직
     */
    fun loginLocalUser(
        request: LocalLoginRequest,
        response: HttpServletResponse
    ): ResponseEntity<ResponseData<AuthResponse>> = processLogin(localAuthService.findUser(request), response)

    @PreAuthorize("isAuthenticated()")
    fun logoutUser(
        request: HttpServletRequest, response: HttpServletResponse, userId: String
    ): ResponseEntity<ResponseInfo> {
        val refreshToken = jwtService.deleteRefreshToken(request, userId)
        response.addCookie(refreshToken)

        SecurityContextHolder.clearContext()
        val headers = jwtService.generateExpiredAccessToken(userId)

        return ResponseEntity(ResponseInfo.to(Code.OK, "Logout successful"), headers, HttpStatus.OK)
    }

    @PreAuthorize("isAuthenticated()")
    fun updatePassword(
        request: PasswordUpdateRequest, userId: String
    ): ResponseData<Boolean> = localAuthService.updatePassword(request, userId)

    fun signInUser(request: LocalRegisterRequest): ResponseData<Boolean> = localAuthService.saveUser(request)

    private fun processLogin(
        loginResult: Pair<Auth, Boolean>,
        response: HttpServletResponse
    ): ResponseEntity<ResponseData<AuthResponse>> {
        val (auth, isNewAuth) = loginResult
        val user = auth.activeUser

        if (isNewAuth) {
            sendWelcomeMailIfNeeded(auth)
        }

        val headers = jwtService.generateToken(user)
        val refreshTokenCookie = jwtService.generateRefreshToken(user)
        response.addCookie(refreshTokenCookie)

        return ResponseEntity(ResponseData.to(AuthResponse.from(auth)), headers, HttpStatus.OK)
    }

    /**
     * todo
     *  html 코드 부분 나중에 대체할 것!
     */
    private fun sendWelcomeMailIfNeeded(auth: Auth) {
        if (auth.isNewUser) {
            mailService.sendHtmlMail(
                to = auth.email,
                subject = "가입을 축하드립니다!",
                html = "<h1>Welcome!</h1><p>가입해주셔서 감사합니다.</p>"
            )

            log.info("가입 축하 메일을 전송하였습니다. authId: {}, to: {}", auth.id, auth.email)
        }
    }
}