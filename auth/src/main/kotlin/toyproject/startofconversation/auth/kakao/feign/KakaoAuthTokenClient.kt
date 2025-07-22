package toyproject.startofconversation.auth.kakao.feign

import org.hibernate.annotations.Comment
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestParam
import toyproject.startofconversation.auth.kakao.dto.KakaoProfile
import toyproject.startofconversation.auth.kakao.dto.OAuthToken

@FeignClient(name = "kakaoAuthTokenClient", url = "https://kauth.kakao.com")
interface KakaoAuthTokenClient {

    @PostMapping("/oauth/token")
    fun getAccessToken(
        @RequestParam("grant_type") grantType: String = "authorization_code",
        @RequestParam("code") code: String,
        @RequestParam("client_id") clientId: String,
        @RequestParam("redirect_uri") redirectUri: String
    ): OAuthToken

    @Comment("Kakao Profile 요청")
    @GetMapping("/v2/user/me")
    fun requestProfile(@RequestHeader("Authorization") authorization: String): KakaoProfile
}