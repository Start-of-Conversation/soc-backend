package toyproject.startofconversation.api.cardGroup

import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import toyproject.startofconversation.api.cardGroup.dto.CardGroupCreateRequest
import toyproject.startofconversation.api.cardGroup.dto.CardGroupInfoResponse
import toyproject.startofconversation.auth.support.SecurityUtil
import toyproject.startofconversation.common.base.dto.ResponseData

@Tag(name = "CardGroup")
@RestController
@RequestMapping("/api/cardgroup")
class CardGroupController(
    private val cardGroupService: CardGroupService
) {

    @GetMapping("/public")
    fun publicCardGroup(@RequestParam("id") cardGroupId: String): ResponseData<CardGroupInfoResponse> =
        cardGroupService.getCardGroupInfo(cardGroupId)

    @PostMapping("/add")
    fun createCardGroup(@RequestBody request: CardGroupCreateRequest): ResponseData<CardGroupInfoResponse> =
        cardGroupService.create(request, SecurityUtil.getCurrentUserId())
}