package toyproject.startofconversation.auth.service

import org.springframework.stereotype.Service
import toyproject.startofconversation.auth.apple.service.AppleAuthService
import toyproject.startofconversation.auth.controller.dto.OAuthParameter
import toyproject.startofconversation.auth.domain.entity.Auth
import toyproject.startofconversation.auth.domain.entity.value.AuthProvider
import toyproject.startofconversation.auth.domain.entity.value.AuthProvider.*
import toyproject.startofconversation.auth.kakao.service.KakaoAuthService
import toyproject.startofconversation.auth.naver.service.NaverAuthService

@Service
class SocialLoginService(
    private val appleAuthService: AppleAuthService,
    private val kakaoAuthService: KakaoAuthService,
    private val naverAuthService: NaverAuthService
) {

    fun getOauthParams(provider: AuthProvider): OAuthParameter = when (provider) {
        APPLE -> appleAuthService.getParameters()
        KAKAO -> kakaoAuthService.getParameters()
        NAVER -> naverAuthService.getParameters()
    }

    // 공통된 소셜 로그인 처리 로직
    fun handleSocialLogin(
        code: String,
        state: String,
        authProvider: AuthProvider
    ): Auth {
        val auth = when (authProvider) {
            APPLE -> appleAuthService.loadUser(code)
            KAKAO -> kakaoAuthService.loadUser(code)
            NAVER -> naverAuthService.loadUser(code, state)
        }
        return auth
    }
}