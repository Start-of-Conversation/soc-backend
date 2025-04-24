package toyproject.startofconversation.auth.service

import org.springframework.stereotype.Service
import toyproject.startofconversation.auth.apple.service.AppleAuthService
import toyproject.startofconversation.auth.domain.entity.Auth
import toyproject.startofconversation.auth.domain.entity.value.AuthProvider
import toyproject.startofconversation.auth.kakao.service.KakaoAuthService

@Service
class SocialLoginService(
    private val appleAuthService: AppleAuthService,
    private val kakaoAuthService: KakaoAuthService
) {

    // 공통된 소셜 로그인 처리 로직
    fun handleSocialLogin(
        code: String,
        authProvider: AuthProvider
    ): Auth {
        val auth = when (authProvider) {
            AuthProvider.APPLE -> appleAuthService.loadUser(code)
            AuthProvider.KAKAO -> kakaoAuthService.loadUser(code)
            else -> throw IllegalArgumentException("Unsupported auth provider: $authProvider")
        }
        return auth
    }
}