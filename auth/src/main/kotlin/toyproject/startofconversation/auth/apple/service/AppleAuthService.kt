package toyproject.startofconversation.auth.apple.service

import org.springframework.stereotype.Service
import toyproject.startofconversation.auth.apple.dto.AppleOAuthProperties
import toyproject.startofconversation.auth.apple.dto.AppleTokenResponse
import toyproject.startofconversation.auth.apple.dto.AppleUser
import toyproject.startofconversation.auth.apple.feign.AppleUserClient
import toyproject.startofconversation.auth.controller.dto.OAuthParameter
import toyproject.startofconversation.auth.domain.entity.Auth
import toyproject.startofconversation.auth.domain.entity.value.AuthProvider
import toyproject.startofconversation.auth.service.OAuthService
import toyproject.startofconversation.auth.service.UserAuthStore
import toyproject.startofconversation.auth.support.RandomNameMaker
import toyproject.startofconversation.common.exception.SOCUnauthorizedException
import toyproject.startofconversation.common.transaction.helper.Tx

@Service
class AppleAuthService(
    private val appleUserClient: AppleUserClient,
    private val oAuthClientService: AppleOAuthClientService,
    private val appleOAuthProperties: AppleOAuthProperties,
    private val identityParser: AppleIdentityParser,
    private val store: UserAuthStore
) : OAuthService {

    override fun getParameters(): OAuthParameter = OAuthParameter(
        clientId = appleOAuthProperties.aud,
        redirectUri = appleOAuthProperties.redirectUri,
        responseType = "code",
        scope = "name email",
        responseMode = "form_post"
    )

    @Throws(SOCUnauthorizedException::class)
    fun loadUser(authorizationCode: String): Auth = Tx.writeTx {
        val tokenResponse = oAuthClientService.requestToken(authorizationCode)
        val accountId: String = identityParser.getAppleAccountId(tokenResponse.id_token)

        store.findOrCreateAuth(provider = AuthProvider.APPLE, authId = accountId) {
            createAndSaveAuth(tokenResponse, accountId)
        }
    }

    private fun createAndSaveAuth(tokenResponse: AppleTokenResponse, accountId: String): Auth {
        val response = appleUserClient.getAppleUserInfo("Bearer ${tokenResponse.access_token}")
        val email = response.email ?: throw SOCUnauthorizedException("Email is required")
        val name = buildName(response)

        val existingUser = store.getOrCreateUser(email = email, name = name)

        return store.saveAuth(
            user = existingUser,
            email = email,
            authProvider = AuthProvider.APPLE,
            authId = accountId
        )
    }

    private fun buildName(userInfo: AppleUser): String =
        "${userInfo.name.lastName ?: ""} ${userInfo.name.firstName ?: ""}".trim()
            .ifBlank { RandomNameMaker.generate() }
}