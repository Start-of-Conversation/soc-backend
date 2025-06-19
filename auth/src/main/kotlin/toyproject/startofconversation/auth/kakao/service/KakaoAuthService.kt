package toyproject.startofconversation.auth.kakao.service

import org.springframework.stereotype.Service
import toyproject.startofconversation.auth.controller.dto.OAuthParameter
import toyproject.startofconversation.auth.domain.entity.Auth
import toyproject.startofconversation.auth.domain.entity.value.AuthProvider
import toyproject.startofconversation.auth.kakao.dto.KakaoAccount
import toyproject.startofconversation.auth.kakao.dto.KakaoOAuthProperties
import toyproject.startofconversation.auth.kakao.dto.KakaoProfile
import toyproject.startofconversation.auth.kakao.dto.OAuthToken
import toyproject.startofconversation.auth.kakao.feign.KakaoAuthTokenClient
import toyproject.startofconversation.auth.service.OAuthService
import toyproject.startofconversation.auth.service.UserAuthStore
import toyproject.startofconversation.auth.support.RandomNameMaker
import toyproject.startofconversation.common.exception.SOCUnauthorizedException
import toyproject.startofconversation.common.transaction.helper.Tx
import java.util.UUID

@Service
class KakaoAuthService(
    private val kakaoAuthTokenClient: KakaoAuthTokenClient,
    private val kakaoOAuthProperties: KakaoOAuthProperties,
    private val store: UserAuthStore
) : OAuthService {

    override fun getParameters(): OAuthParameter = OAuthParameter(
        clientId = kakaoOAuthProperties.clientId,
        redirectUri = kakaoOAuthProperties.redirectUri,
        responseType = "code",
        scope = "profile_nickname profile_image account_email",
        state = UUID.randomUUID().toString() // 필요에 따라 사용
    )

    fun loadUser(accessCode: String): Auth = Tx.writeTx {
        val oAuthToken = requestToken(accessCode)

        val authorization = "Bearer ${oAuthToken.access_token}"
        val kakaoProfile = kakaoAuthTokenClient.requestProfile(authorization)

        store.findOrCreateAuth(
            provider = AuthProvider.KAKAO,
            authId = kakaoProfile.id.toString()
        ) { createAndSaveAuth(kakaoProfile) }
    }

    private fun requestToken(accessCode: String): OAuthToken = kakaoAuthTokenClient.getAccessToken(
        clientId = kakaoOAuthProperties.clientId,
        redirectUri = kakaoOAuthProperties.redirectUri,
        code = accessCode
    )

    private fun createAndSaveAuth(kakaoProfile: KakaoProfile): Auth {
        val profile = kakaoProfile.kakao_account.profile

        //이름 생성
        val name = if (profile.nickname.isNullOrBlank()) RandomNameMaker.generate() else profile.nickname

        //이메일 처리
        val email = extractVerifiedEmail(kakaoProfile.kakao_account)

        //user 저장
        val user = store.getOrCreateUser(email = email, name = name)
        val kakaoId = kakaoProfile.id.toString()

        return store.saveAuth(
            user = user,
            email = email,
            authProvider = AuthProvider.KAKAO,
            authId = kakaoId
        )
    }

    private fun extractVerifiedEmail(account: KakaoAccount): String = with(account) {
        return if (
            has_email &&
            is_email_verified &&
            !email.isNullOrBlank()
        ) {
            email
        } else {
            throw SOCUnauthorizedException("Verified email is required")
        }
    }

}