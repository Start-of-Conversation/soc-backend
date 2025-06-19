package toyproject.startofconversation.auth.naver.service

import org.springframework.stereotype.Service
import toyproject.startofconversation.auth.controller.dto.OAuthParameter
import toyproject.startofconversation.auth.domain.entity.Auth
import toyproject.startofconversation.auth.domain.entity.value.AuthProvider
import toyproject.startofconversation.auth.naver.dto.NaverOAuthProperties
import toyproject.startofconversation.auth.naver.dto.NaverTokenRequest
import toyproject.startofconversation.auth.naver.dto.NaverUserInfoResponse
import toyproject.startofconversation.auth.naver.feign.NaverAuthTokenClient
import toyproject.startofconversation.auth.naver.feign.NaverUserInfoClient
import toyproject.startofconversation.auth.service.OAuthService
import toyproject.startofconversation.auth.service.UserAuthStore
import toyproject.startofconversation.auth.support.RandomNameMaker
import toyproject.startofconversation.common.transaction.helper.Tx
import java.util.UUID
import javax.security.sasl.AuthenticationException

@Service
class NaverAuthService(
    private val naverOAuthProperties: NaverOAuthProperties,
    private val naverAuthTokenClient: NaverAuthTokenClient,
    private val naverUserInfoClient: NaverUserInfoClient,
    private val store: UserAuthStore
) : OAuthService {

    override fun getParameters(): OAuthParameter = OAuthParameter(
        clientId = naverOAuthProperties.params.clientId,
        redirectUri = naverOAuthProperties.path.redirectUri,
        responseType = "code",
        state = UUID.randomUUID().toString()
    )

    fun loadUser(token: String, state: String): Auth = Tx.writeTx {
        val tokenResponse = naverAuthTokenClient.getAccessToken(
            NaverTokenRequest(
                client_id = naverOAuthProperties.params.clientId,
                client_secret = naverOAuthProperties.params.clientSecret,
                code = token,
                state = state
            )
        )

        val userInfo = naverUserInfoClient.getUserInfo("Bearer ${tokenResponse.accessToken}")

        store.findOrCreateAuth(
            provider = AuthProvider.NAVER, authId = userInfo.response.id
        ) { createAndSaveAuth(userInfo) }
    }

    private fun createAndSaveAuth(userInfo: NaverUserInfoResponse): Auth {
        val email = userInfo.response.email ?: throw AuthenticationException("Email is required")
        val name = userInfo.response.name ?: RandomNameMaker.generate()
        val existingUser = store.getOrCreateUser(email = email, name = name)

        return store.saveAuth(
            user = existingUser,
            email = email,
            authProvider = AuthProvider.NAVER,
            authId = userInfo.response.id
        )
    }
}