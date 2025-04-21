package toyproject.startofconversation.auth.controller

import jakarta.servlet.http.HttpServletResponse
import jakarta.servlet.http.HttpSession
import lombok.RequiredArgsConstructor
import org.hibernate.annotations.Comment
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import toyproject.startofconversation.auth.apple.dto.AppleAuthRequest
import toyproject.startofconversation.auth.apple.service.AppleAuthService
import toyproject.startofconversation.auth.domain.entity.Auth
import toyproject.startofconversation.auth.service.AuthService
import toyproject.startofconversation.common.base.dto.ResponseData

@RestController("/auth")
@RequiredArgsConstructor
class AuthController(
    private val appleAuthService: AppleAuthService,
    private val authService: AuthService
) {

    @Comment("apple 소셜 로그인 구현")
    @PostMapping("/apple")
    fun loginAppleUser(
        @RequestBody request: AppleAuthRequest,
        session: HttpSession,
        response: HttpServletResponse
    ): ResponseEntity<ResponseData<Auth>> {
        val auth = appleAuthService.loadUser(request)
        val headers = authService.generateToken(auth.user)
        val refreshTokenCookie = authService.generateRefreshToken(auth.user)

        response.addCookie(refreshTokenCookie)

        return ResponseEntity(ResponseData.to(auth), headers, HttpStatus.OK)
    }

}