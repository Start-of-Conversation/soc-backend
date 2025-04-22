package toyproject.startofconversation.auth.apple.service

import jakarta.transaction.Transactional
import lombok.RequiredArgsConstructor
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import toyproject.startofconversation.auth.apple.feign.AppleUserClient
import toyproject.startofconversation.auth.apple.ApplePublicKeyGenerator
import toyproject.startofconversation.auth.apple.dto.AppleTokenResponse
import toyproject.startofconversation.auth.apple.feign.AppleAuthClient
import toyproject.startofconversation.auth.apple.feign.AppleAuthTokenClient
import toyproject.startofconversation.auth.apple.provider.AppleJwtProvider
import toyproject.startofconversation.auth.domain.entity.Auth
import toyproject.startofconversation.auth.domain.entity.value.AuthProvider
import toyproject.startofconversation.auth.domain.repository.AuthRepository
import toyproject.startofconversation.auth.util.RandomNameMaker
import toyproject.startofconversation.common.domain.user.entity.Users
import toyproject.startofconversation.common.domain.user.repository.UsersRepository
import java.security.PublicKey
import javax.security.sasl.AuthenticationException

@Service
@RequiredArgsConstructor
class AppleAuthService(
    private val appleUserClient: AppleUserClient,
    private val appleAuthClient: AppleAuthClient,
    private val appleAuthTokenClient: AppleAuthTokenClient,
    private val applePublicKeyGenerator: ApplePublicKeyGenerator,
    private val appleJwtProvider: AppleJwtProvider,
    private val authRepository: AuthRepository,
    private val usersRepository: UsersRepository
) {

    @Value("\${social.apple.client-id}")
    lateinit var clientId: String // 애플에서 발급받은 Client ID

    @Value("\${social.apple.client-secret}")
    lateinit var clientSecret: String // 애플에서 발급받은 Client Secret

    @Value("\${social.apple.redirect-uri}")
    lateinit var redirectUri: String // 애플 개발자 콘솔에 설정된 Redirect URI

    @Transactional
    @Throws(AuthenticationException::class)
    fun loadUser(authorizationCode: String): Auth {
        val tokenResponse = requestAccessToken(authorizationCode)

        val appleUserInfo = appleUserClient.getAppleUserInfo("Bearer $tokenResponse.accessToken")

        //accountId 가져오기
        val accountID: String = getAppleAccountId(tokenResponse.idToken)
        authRepository.findByAuthId(accountID)?.let { return it }

        //이름 생성
        var name = (appleUserInfo.name.lastName ?: "") + " " + (appleUserInfo.name.firstName ?: "")
        if (name.isBlank()) {
            name = RandomNameMaker.generate()
        }
        //이메일 처리
        val email = appleUserInfo.email ?: throw AuthenticationException("Email is required")

        //user 저장
        val user = usersRepository.findByEmail(email) ?: usersRepository.save(
            Users.to(email = email, nickname = name)
        )

        return authRepository.save(
            Auth(
                user = user,
                authProvider = AuthProvider.APPLE,
                authId = appleUserInfo.sub
            )
        )
    }

    @Throws(AuthenticationException::class)
    private fun requestAccessToken(authorizationCode: String): AppleTokenResponse = appleAuthTokenClient.getAccessToken(
        grantType = "authorization_code",
        code = authorizationCode,
        clientId = clientId,
        clientSecret = clientSecret,
        redirectUri = redirectUri
    )

    private fun getAppleAccountId(identityToken: String): String {
        val headers: Map<String, String> = appleJwtProvider.parseHeaders(identityToken)
        //Feign Client를 사용하여 공개키 요청
        val publicKey: PublicKey =
            applePublicKeyGenerator.generatePublicKey(headers, appleAuthClient.getAppleAuthPublicKey())
        return appleJwtProvider.getTokenClaims(identityToken, publicKey).subject
    }

}