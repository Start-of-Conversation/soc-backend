package toyproject.startofconversation.auth.apple.service

import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import toyproject.startofconversation.auth.apple.dto.AppleOAuthProperties
import toyproject.startofconversation.auth.apple.dto.AppleUser
import toyproject.startofconversation.auth.apple.feign.AppleUserClient
import toyproject.startofconversation.auth.controller.dto.OAuthParameter
import toyproject.startofconversation.auth.domain.entity.Auth
import toyproject.startofconversation.auth.domain.entity.value.AuthProvider
import toyproject.startofconversation.auth.domain.repository.AuthRepository
import toyproject.startofconversation.auth.service.OAuthService
import toyproject.startofconversation.auth.util.RandomNameMaker
import toyproject.startofconversation.common.domain.user.entity.Users
import toyproject.startofconversation.common.domain.user.repository.UsersRepository
import javax.security.sasl.AuthenticationException

@Service
class AppleAuthService(
    private val appleUserClient: AppleUserClient,
    private val oAuthClientService: AppleOAuthClientService,
    private val appleOAuthProperties: AppleOAuthProperties,
    private val identityParser: AppleIdentityParser,
    private val authRepository: AuthRepository,
    private val usersRepository: UsersRepository
) : OAuthService {

    override fun getParameters(): OAuthParameter = OAuthParameter(
        clientId = appleOAuthProperties.aud,
        redirectUri = appleOAuthProperties.redirectUri,
        responseType = "code",
        scope = "name email",
        responseMode = "form_post"
    )

    @Transactional
    @Throws(AuthenticationException::class)
    fun loadUser(authorizationCode: String): Auth {
        val tokenResponse = oAuthClientService.requestToken(authorizationCode)
        val accountID: String = identityParser.getAppleAccountId(tokenResponse.id_token)

        authRepository.findByAuthProviderAndAuthId(AuthProvider.APPLE, accountID)?.let { return it }

        val appleUserInfo = appleUserClient.getAppleUserInfo("Bearer ${tokenResponse.access_token}")
        var name = buildName(appleUserInfo)
        val email = appleUserInfo.email ?: throw AuthenticationException("Email is required")

        val existingUser = authRepository.findByEmail(email)?.user
            ?: usersRepository.save(Users(nickname = name))

        return authRepository.save(
            Auth(
                user = existingUser,
                email = email,
                authProvider = AuthProvider.APPLE,
                authId = accountID
            )
        )
    }

    private fun buildName(userInfo: AppleUser): String =
        "${userInfo.name.lastName ?: ""} ${userInfo.name.firstName ?: ""}".trim()
            .ifBlank { RandomNameMaker.generate() }
}