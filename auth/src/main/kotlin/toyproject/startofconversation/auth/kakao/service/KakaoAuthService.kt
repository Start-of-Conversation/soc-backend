package toyproject.startofconversation.auth.kakao.service

import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.AuthenticationException
import org.springframework.stereotype.Service
import toyproject.startofconversation.auth.domain.entity.Auth
import toyproject.startofconversation.auth.domain.entity.value.AuthProvider
import toyproject.startofconversation.auth.domain.repository.AuthRepository
import toyproject.startofconversation.auth.kakao.dto.OAuthToken
import toyproject.startofconversation.auth.kakao.feign.KakaoAuthTokenClient
import toyproject.startofconversation.auth.util.RandomNameMaker
import toyproject.startofconversation.common.domain.user.entity.Users
import toyproject.startofconversation.common.domain.user.repository.UsersRepository
import toyproject.startofconversation.common.exception.SOCUnauthorizedException

@Service
class KakaoAuthService(
    private val kakaoAuthTokenClient: KakaoAuthTokenClient,
    private val usersRepository: UsersRepository,
    private val authRepository: AuthRepository,
) {
    @Value("\${social.kakao.client_id}")
    private lateinit var clientId: String

    @Value("\${social.kakao.redirect_uri}")
    private lateinit var redirect: String

    @Throws(AuthenticationException::class)
    fun loadUser(accessCode: String): Auth {
        val oAuthToken = requestToken(accessCode)

        val authorization = "Bearer ${oAuthToken.access_token}"
        val kakaoProfile = kakaoAuthTokenClient.requestProfile(authorization)

        val profile = kakaoProfile.kakao_account.profile

        authRepository.findByAuthId(kakaoProfile.id.toString())?.let { return it }

        //이름 생성
        val name = if (profile.nickname.isNullOrBlank()) RandomNameMaker.generate() else profile.nickname

        //이메일 처리
        val email =
            if (kakaoProfile.kakao_account.has_email && kakaoProfile.kakao_account.is_email_verified) {
                kakaoProfile.kakao_account.email  // 이메일이 존재하고, 검증된 경우 이메일 사용
            } else {
                throw SOCUnauthorizedException("Email is required or not verified")  // 이메일이 없거나 검증되지 않은 경우 예외 처리
            } ?: throw SOCUnauthorizedException("Email is null")

        //user 저장
        val user = usersRepository.findByEmail(email) ?: usersRepository.save(
            Users.to(email = email, nickname = name)
        )

        return authRepository.save(
            Auth(
                user = user,
                authProvider = AuthProvider.KAKAO,
                authId = kakaoProfile.id.toString()
            )
        )

    }

    @Throws(AuthenticationException::class)
    fun requestToken(accessCode: String): OAuthToken = kakaoAuthTokenClient.getAccessToken(
        clientId = clientId,
        redirectUri = redirect,
        code = accessCode
    )

}