package toyproject.startofconversation.api.user.controller

import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import toyproject.startofconversation.api.user.dto.MarketingUpdateRequest
import toyproject.startofconversation.api.user.service.MarketingService
import toyproject.startofconversation.auth.util.SecurityUtil
import toyproject.startofconversation.common.base.dto.ResponseData

@RestController
@RequestMapping("/api/marketing")
class MarketingController(
    private val marketingService: MarketingService
) {

    @PatchMapping("/update")
    fun updateMarketing(@RequestBody request: MarketingUpdateRequest): ResponseData<Boolean> =
        marketingService.updateMarketing(SecurityUtil.getCurrentUserId(), request)

}