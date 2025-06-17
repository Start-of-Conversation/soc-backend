package toyproject.startofconversation.api.user.controller

import org.springframework.web.bind.annotation.*
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