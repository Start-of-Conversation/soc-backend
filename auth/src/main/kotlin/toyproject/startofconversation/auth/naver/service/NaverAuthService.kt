package toyproject.startofconversation.auth.naver.service

import toyproject.startofconversation.auth.controller.dto.OAuthParameter
import toyproject.startofconversation.auth.naver.dto.NaverOAuthProperties
import toyproject.startofconversation.auth.service.OAuthService
import java.util.UUID

class NaverAuthService(
    private val naverOAuthProperties: NaverOAuthProperties
) : OAuthService {

    override fun getParameters(): OAuthParameter = OAuthParameter(
        clientId = naverOAuthProperties.params.clientId,
        redirectUri = naverOAuthProperties.path.redirectUri,
        responseType = "code",
        state = UUID.randomUUID().toString()
    )
}