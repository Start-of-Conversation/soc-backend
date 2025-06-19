package toyproject.startofconversation.api.user.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import toyproject.startofconversation.api.common.BaseController
import toyproject.startofconversation.api.user.dto.MarketingResponse
import toyproject.startofconversation.api.user.dto.MarketingUpdateRequest
import toyproject.startofconversation.api.user.service.MarketingService
import toyproject.startofconversation.common.base.dto.ResponseData

@RestController
@RequestMapping("/api/marketing")
class MarketingController(
    private val marketingService: MarketingService
) : BaseController() {

    @GetMapping
    fun getMarketing(): ResponseData<MarketingResponse> =
        marketingService.getMarketingInfo(getUserId())

    @PatchMapping("/update")
    fun updateMarketing(@RequestBody request: MarketingUpdateRequest): ResponseData<MarketingResponse> =
        marketingService.updateMarketing(getUserId(), request)

}