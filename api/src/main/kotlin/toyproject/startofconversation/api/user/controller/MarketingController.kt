package toyproject.startofconversation.api.user.controller

import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import toyproject.startofconversation.api.user.dto.MarketingResponse
import toyproject.startofconversation.api.user.dto.MarketingUpdateRequest
import toyproject.startofconversation.api.user.service.MarketingService
import toyproject.startofconversation.common.base.controller.BaseController
import toyproject.startofconversation.common.base.dto.ResponseData

@Tag(name = "Marketing")
@RestController
@RequestMapping("/api/marketing")
@SecurityRequirement(name = "bearerAuth")
class MarketingController(
    private val marketingService: MarketingService
) : BaseController() {

    @GetMapping
    fun getMarketing(): ResponseData<MarketingResponse> =
        marketingService.getMarketingInfo(getUserId())

    @PatchMapping
    fun updateMarketing(@RequestBody request: MarketingUpdateRequest): ResponseData<MarketingResponse> =
        marketingService.updateMarketingWithFCM(getUserId(), request)

}