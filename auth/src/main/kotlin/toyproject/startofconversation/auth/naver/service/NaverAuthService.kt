package toyproject.startofconversation.auth.naver.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import toyproject.startofconversation.auth.controller.dto.OAuthParameter
import toyproject.startofconversation.auth.domain.entity.Auth
import toyproject.startofconversation.auth.domain.entity.value.AuthProvider
import toyproject.startofconversation.auth.domain.repository.AuthRepository
import toyproject.startofconversation.auth.naver.dto.NaverOAuthProperties
import toyproject.startofconversation.auth.naver.dto.NaverTokenRequest
import toyproject.startofconversation.auth.naver.feign.NaverAuthTokenClient
import toyproject.startofconversation.auth.naver.feign.NaverUserInfoClient
import toyproject.startofconversation.auth.service.OAuthService
import toyproject.startofconversation.auth.util.RandomNameMaker
import toyproject.startofconversation.common.domain.user.entity.Users
import toyproject.startofconversation.common.domain.user.repository.UsersRepository
import java.util.*
import javax.security.sasl.AuthenticationException

@Service
class NaverAuthService(
    private val naverOAuthProperties: NaverOAuthProperties,
    private val naverAuthTokenClient: NaverAuthTokenClient,
    private val naverUserInfoClient: NaverUserInfoClient,
    private val authRepository: AuthRepository,
    private val usersRepository: UsersRepository,
) : OAuthService {

    override fun getParameters(): OAuthParameter = OAuthParameter(
        clientId = naverOAuthProperties.params.clientId,
        redirectUri = naverOAuthProperties.path.redirectUri,
        responseType = "code",
        state = UUID.randomUUID().toString()
    )

    @Transactional
    fun loadUser(token: String, state: String): Auth {
        val tokenResponse = naverAuthTokenClient.getAccessToken(
            NaverTokenRequest(
                client_id = naverOAuthProperties.params.clientId,
                client_secret = naverOAuthProperties.params.clientSecret,
                code = token,
                state = state
            )
        )

        val userInfo = naverUserInfoClient.getUserInfo("Bearer ${tokenResponse.accessToken}")

        val email = userInfo.response.email ?: throw AuthenticationException("Email is required")
        val name = userInfo.response.name ?: RandomNameMaker.generate()

        val existingUser = authRepository.findByEmail(email)?.user
            ?: usersRepository.save(Users(nickname = name).createMarketing())

        return authRepository.save(
            Auth(
                user = existingUser,
                email = email,
                authProvider = AuthProvider.NAVER,
                authId = userInfo.response.id
            )
        )
    }
}