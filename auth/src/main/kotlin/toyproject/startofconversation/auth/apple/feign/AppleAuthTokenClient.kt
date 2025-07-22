package toyproject.startofconversation.auth.apple.feign

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import toyproject.startofconversation.auth.apple.dto.AppleTokenResponse

@FeignClient(name = "appleAuthTokenClient", url = "\${social.apple.auth.token-url:http://localhost:8080/fake-apple}")
interface AppleAuthTokenClient {

    @PostMapping
    fun getAccessToken(
        @RequestParam("grant_type") grantType: String = "authorization_code",
        @RequestParam("code") code: String,
        @RequestParam("client_id") clientId: String,
        @RequestParam("client_secret") clientSecret: String,
        @RequestParam("redirect_uri") redirectUri: String
    ): AppleTokenResponse
}