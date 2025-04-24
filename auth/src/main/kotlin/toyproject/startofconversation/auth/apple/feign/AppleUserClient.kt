package toyproject.startofconversation.auth.apple.feign

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader
import toyproject.startofconversation.auth.apple.dto.AppleUser

@FeignClient(name = "appleUserClient", url = "https://api.apple.com/v1")
interface AppleUserClient {

    @GetMapping("/userinfo")
    fun getAppleUserInfo(
        @RequestHeader("Authorization") authorization: String
    ) : AppleUser
}