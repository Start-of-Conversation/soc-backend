package toyproject.startofconversation.auth.apple.service

import io.jsonwebtoken.Jwts
import jakarta.transaction.Transactional
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import toyproject.startofconversation.auth.apple.ApplePublicKeyGenerator
import toyproject.startofconversation.auth.apple.dto.AppleTokenResponse
import toyproject.startofconversation.auth.apple.feign.AppleAuthClient
import toyproject.startofconversation.auth.apple.feign.AppleAuthTokenClient
import toyproject.startofconversation.auth.apple.feign.AppleUserClient
import toyproject.startofconversation.auth.apple.provider.AppleJwtProvider
import toyproject.startofconversation.auth.controller.dto.OAuthParameter
import toyproject.startofconversation.auth.domain.entity.Auth
import toyproject.startofconversation.auth.domain.entity.value.AuthProvider
import toyproject.startofconversation.auth.domain.repository.AuthRepository
import toyproject.startofconversation.auth.service.OAuthService
import toyproject.startofconversation.auth.util.RandomNameMaker
import toyproject.startofconversation.common.domain.user.entity.Users
import toyproject.startofconversation.common.domain.user.repository.UsersRepository
import java.nio.file.Files
import java.nio.file.Paths
import java.security.KeyFactory
import java.security.PublicKey
import java.security.interfaces.RSAPrivateKey
import java.util.*
import javax.security.sasl.AuthenticationException

@Service
class AppleAuthService (
    private val appleUserClient: AppleUserClient,
    private val appleAuthClient: AppleAuthClient,
    private val appleAuthTokenClient: AppleAuthTokenClient,
    private val applePublicKeyGenerator: ApplePublicKeyGenerator,
    private val appleJwtProvider: AppleJwtProvider,
    private val authRepository: AuthRepository,
    private val usersRepository: UsersRepository
) : OAuthService {

    @Value("\${social.apple.aud}")
    private lateinit var clientId: String // 애플에서 발급받은 Client ID

    @Value("\${social.apple.team-id}")
    private lateinit var teamId: String

    @Value("\${social.apple.key.path}")
    private lateinit var keyPath: String

    @Value("\${social.apple.redirect-uri}")
    private lateinit var redirectUri: String // 애플 개발자 콘솔에 설정된 Redirect URI

    override fun getParameters() : OAuthParameter = OAuthParameter(
            clientId = clientId,
            redirectUri = redirectUri,
            responseType = "code",
            scope = "name email",
            responseMode = "form_post"
        )

    @Transactional
    @Throws(AuthenticationException::class)
    fun loadUser(authorizationCode: String): Auth {
        val tokenResponse = requestAccessToken(authorizationCode)

        val appleUserInfo = appleUserClient.getAppleUserInfo("Bearer ${tokenResponse.access_token}")

        //accountId 가져오기
        val accountID: String = getAppleAccountId(tokenResponse.id_token)
        authRepository.findByAuthProviderAndAuthId(AuthProvider.APPLE, accountID)?.let { return it }

        //이름 생성
        var name = (appleUserInfo.name.lastName ?: "") + " " + (appleUserInfo.name.firstName ?: "")
        if (name.isBlank()) {
            name = RandomNameMaker.generate()
        }

        // 아래는 최초 로그인일 때만 실행됨

        //이메일 처리
        val email = appleUserInfo.email ?: throw AuthenticationException("Email is required")
        val authByEmail = email.let { authRepository.findByEmail(it) }
        val newUser = authByEmail?.user ?: usersRepository.save(Users(nickname = name))

        return authRepository.save(
            Auth(
                user = newUser,
                email = email,
                authProvider = AuthProvider.APPLE,
                authId = accountID
            )
        )
    }

    @Throws(AuthenticationException::class)
    private fun requestAccessToken(authorizationCode: String): AppleTokenResponse = appleAuthTokenClient.getAccessToken(
        code = authorizationCode,
        clientId = clientId,
        clientSecret = generateClientSecret(),
        redirectUri = redirectUri
    )

    private fun getAppleAccountId(identityToken: String): String {
        val headers: Map<String, String> = appleJwtProvider.parseHeaders(identityToken)
        val publicKey: PublicKey =
            applePublicKeyGenerator.generatePublicKey(headers, appleAuthClient.getAppleAuthPublicKey())
        return appleJwtProvider.getTokenClaims(identityToken, publicKey).subject
    }

    private fun generateClientSecret(): String = Jwts.builder()
        .issuer(teamId)  // 팀 ID
        .setAudience(clientId)  // 클라이언트 ID
        .issuedAt(Date())  // 발급일
        .expiration(Date(System.currentTimeMillis() + 6 * 30L * 24 * 60 * 60 * 1000))  // 만료일 (6개월)
        .subject("appleAuth")  // 주체
        .signWith(loadPrivateKey(), Jwts.SIG.RS256)  // 서명
        .compact()

    private fun loadPrivateKey(): RSAPrivateKey {
        val keyBytes = Files.readAllBytes(Paths.get(keyPath))
        val keySpec = java.security.spec.PKCS8EncodedKeySpec(keyBytes)
        val keyFactory = KeyFactory.getInstance("RSA")
        return keyFactory.generatePrivate(keySpec) as RSAPrivateKey
    }

}