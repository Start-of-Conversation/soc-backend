package toyproject.startofconversation.auth.naver.feign

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import toyproject.startofconversation.auth.config.FeignFormSupportConfig
import toyproject.startofconversation.auth.naver.dto.NaverOauthTokenResponse
import toyproject.startofconversation.auth.naver.dto.NaverTokenRequest

@FeignClient(
    name = "naverAuthTokenClient",
    url = "\${social.naver.path.tokenUrl}",
    configuration = [FeignFormSupportConfig::class]
)
interface NaverAuthTokenClient {

    @PostMapping(
        consumes = [MediaType.APPLICATION_FORM_URLENCODED_VALUE],
        headers = ["Content-Type=application/x-www-form-urlencoded"]
    )
    fun getAccessToken(@ModelAttribute request: NaverTokenRequest): NaverOauthTokenResponse
}