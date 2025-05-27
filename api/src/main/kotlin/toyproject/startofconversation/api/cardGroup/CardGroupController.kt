package toyproject.startofconversation.api.cardGroup

import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import toyproject.startofconversation.api.cardGroup.dto.CardGroupInfoResponse
import toyproject.startofconversation.api.cardGroup.CardGroupService
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
}