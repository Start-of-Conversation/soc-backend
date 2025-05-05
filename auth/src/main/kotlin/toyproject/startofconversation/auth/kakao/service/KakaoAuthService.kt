package toyproject.startofconversation.auth.kakao.service

import jakarta.transaction.Transactional
import org.springframework.security.core.AuthenticationException
import org.springframework.stereotype.Service
import toyproject.startofconversation.auth.controller.dto.OAuthParameter
import toyproject.startofconversation.auth.domain.entity.Auth
import toyproject.startofconversation.auth.domain.entity.value.AuthProvider
import toyproject.startofconversation.auth.domain.repository.AuthRepository
import toyproject.startofconversation.auth.kakao.dto.KakaoOAuthProperties
import toyproject.startofconversation.auth.kakao.dto.OAuthToken
import toyproject.startofconversation.auth.kakao.feign.KakaoAuthTokenClient
import toyproject.startofconversation.auth.service.OAuthService
import toyproject.startofconversation.auth.util.RandomNameMaker
import toyproject.startofconversation.common.domain.user.entity.Users
import toyproject.startofconversation.common.domain.user.repository.UsersRepository
import toyproject.startofconversation.common.exception.SOCUnauthorizedException
import java.util.*

@Service
class KakaoAuthService(
    private val kakaoAuthTokenClient: KakaoAuthTokenClient,
    private val usersRepository: UsersRepository,
    private val authRepository: AuthRepository,
    private val kakaoOAuthProperties: KakaoOAuthProperties
) : OAuthService {

    override fun getParameters(): OAuthParameter = OAuthParameter(
        clientId = kakaoOAuthProperties.clientId,
        redirectUri = kakaoOAuthProperties.redirectUri,
        responseType = "code",
        scope = "profile_nickname profile_image account_email",
        state = UUID.randomUUID().toString() // 필요에 따라 사용
    )

    @Transactional
    @Throws(AuthenticationException::class)
    fun loadUser(accessCode: String): Auth {
        val oAuthToken = requestToken(accessCode)

        val authorization = "Bearer ${oAuthToken.access_token}"
        val kakaoProfile = kakaoAuthTokenClient.requestProfile(authorization)

        val profile = kakaoProfile.kakao_account.profile

        authRepository.findByAuthProviderAndAuthId(AuthProvider.KAKAO, kakaoProfile.id.toString())?.let { return it }

        //이름 생성
        val name = if (profile.nickname.isNullOrBlank()) RandomNameMaker.generate() else profile.nickname

        //이메일 처리
        val email = if (
            kakaoProfile.kakao_account.has_email &&
            kakaoProfile.kakao_account.is_email_verified &&
            !kakaoProfile.kakao_account.email.isNullOrBlank()
        ) {
            kakaoProfile.kakao_account.email
        } else {
            throw SOCUnauthorizedException("Verified email is required")
        }

        //user 저장
        val user = authRepository.findByEmail(email)?.user ?: usersRepository.save(
            Users(nickname = name)
        )

        return authRepository.save(
            Auth(
                user = user,
                email = email,
                authProvider = AuthProvider.KAKAO,
                authId = kakaoProfile.id.toString()
            )
        )

    }

    @Throws(AuthenticationException::class)
    private fun requestToken(accessCode: String): OAuthToken = kakaoAuthTokenClient.getAccessToken(
        clientId = kakaoOAuthProperties.clientId,
        redirectUri = kakaoOAuthProperties.redirectUri,
        code = accessCode
    )

}