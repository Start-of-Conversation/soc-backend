package toyproject.startofconversation.auth.controller

import jakarta.servlet.http.HttpServletResponse
import lombok.RequiredArgsConstructor
import org.hibernate.annotations.Comment
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import toyproject.startofconversation.auth.apple.service.AppleAuthService
import toyproject.startofconversation.auth.domain.entity.Auth
import toyproject.startofconversation.auth.service.AuthService
import toyproject.startofconversation.common.base.dto.ResponseData
import toyproject.startofconversation.common.exception.SOCAuthException

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
class AuthController(
    private val appleAuthService: AppleAuthService,
    private val authService: AuthService
) {

    @Comment("apple 소셜 로그인 구현")
    @PostMapping("/apple")
    fun loginAppleUser(
        @RequestParam("code") authorizationCode: String,
        response: HttpServletResponse
    ): ResponseEntity<ResponseData<Auth>> {
        try {
            val auth = appleAuthService.loadUser(authorizationCode)
            val headers = authService.generateToken(auth.user)
            val refreshTokenCookie = authService.generateRefreshToken(auth.user)

            response.addCookie(refreshTokenCookie)

            return ResponseEntity(ResponseData.to(auth), headers, HttpStatus.OK)
        } catch (e: Exception) {
            throw SOCAuthException("Authentication failed")
        }
    }

}