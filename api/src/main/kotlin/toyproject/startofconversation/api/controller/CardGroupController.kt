package toyproject.startofconversation.api.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import toyproject.startofconversation.api.dto.CardGroupInfoResponse
import toyproject.startofconversation.api.service.CardGroupService
import toyproject.startofconversation.common.base.dto.ResponseData

@RestController
@RequestMapping("/api/cardgroup")
class CardGroupController(
    private val cardGroupService: CardGroupService
) {

    @GetMapping("/public")
    fun publicCardGroup(@RequestParam("id") cardGroupId: String): ResponseData<CardGroupInfoResponse> =
        cardGroupService.getCardGroupInfo(cardGroupId)
}