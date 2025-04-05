package toyproject.startofconversation.auth.controller

import lombok.RequiredArgsConstructor
import org.hibernate.annotations.Comment
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import toyproject.startofconversation.auth.apple.dto.AppleAuthRequest
import toyproject.startofconversation.auth.apple.service.AppleAuthService
import toyproject.startofconversation.auth.domain.entity.Auth
import toyproject.startofconversation.auth.jwt.JwtProvider
import toyproject.startofconversation.common.base.dto.ResponseData

@RestController("/auth")
@RequiredArgsConstructor
class AuthController(
    private val appleAuthService: AppleAuthService,
    private val jwtProvider: JwtProvider
) {

    @Comment("apple 소셜 로그인 구현")
    @PostMapping("/apple")
    fun loginAppleUser(
        @RequestBody request: AppleAuthRequest
    ): ResponseEntity<ResponseData<Auth>> {
        val auth = appleAuthService.loadUser(request)
        val token = jwtProvider.generateToken(auth.user)

        val responseHeaders = HttpHeaders().apply {
            set("Authorization", "Bearer $token") // 헤더에 토큰을 추가
        }

        return ResponseEntity(ResponseData.to(auth), responseHeaders, HttpStatus.OK)
    }

}