package toyproject.startofconversation.auth.naver

import toyproject.startofconversation.auth.controller.dto.OAuthParameter
import toyproject.startofconversation.auth.service.OAuthService
import java.util.UUID

class NaverAuthService : OAuthService {
    override fun getParameters(): OAuthParameter = OAuthParameter(
        clientId = "your.naver.client.id",
        redirectUri = "https://yourdomain.com/oauth/naver/callback",
        responseType = "code",
        state = UUID.randomUUID().toString()
    )
}