package toyproject.startofconversation.auth.naver.feign

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestHeader
import toyproject.startofconversation.auth.naver.dto.NaverUserInfoResponse

@FeignClient(
    name = "naverUserInfoClient",
    url = "\${social.naver.path.userInfoUrl}"
)
interface NaverUserInfoClient {
    @PostMapping(
        consumes = [MediaType.APPLICATION_FORM_URLENCODED_VALUE],
        headers = ["Content-Type=application/x-www-form-urlencoded"]
    )
    fun getUserInfo(
        @RequestHeader("Authorization") authorization: String
    ): NaverUserInfoResponse
}