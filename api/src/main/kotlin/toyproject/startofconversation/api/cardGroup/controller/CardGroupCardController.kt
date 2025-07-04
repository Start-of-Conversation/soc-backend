package toyproject.startofconversation.api.cardGroup.controller

import io.swagger.v3.oas.annotations.Operation
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.web.PageableDefault
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import toyproject.startofconversation.api.card.dto.CardListResponse
import toyproject.startofconversation.api.cardGroup.dto.AddCardToGroupRequest
import toyproject.startofconversation.api.cardGroup.dto.RemoveCardToGroupRequest
import toyproject.startofconversation.api.cardGroup.service.CardGroupCardService
import toyproject.startofconversation.api.paging.PageResponseData
import toyproject.startofconversation.common.base.controller.BaseController
import toyproject.startofconversation.common.base.dto.ResponseData

@RestController
@RequestMapping("/api/card-group")
class CardGroupCardController(
    private val cardGroupCardService: CardGroupCardService
) : BaseController() {

    @Operation(summary = "카드그룹에 포함된 카드 리스트 조회 (페이징)")
    @GetMapping("/public/{cardGroupId}/cards")
    fun getCardsByCardGroup(
        @PathVariable cardGroupId: String,
        @PageableDefault(size = 20, page = 0, sort = ["createdAt"], direction = Sort.Direction.DESC) pageable: Pageable
    ): PageResponseData<CardListResponse> = cardGroupCardService.getCards(cardGroupId, pageable)

    @Operation(summary = "카드그룹에 카드 추가")
    @PostMapping("/{cardGroupId}/cards")
    fun addCardsInCardGroup(
        @PathVariable cardGroupId: String,
        @RequestBody request: AddCardToGroupRequest
    ): ResponseData<CardListResponse> = cardGroupCardService.addCardToGroup(cardGroupId, request, getUserId())

    @Operation(summary = "카드그룹에서 카드 삭제")
    @DeleteMapping("/{cardGroupId}/cards")
    fun deleteCardsInCardGroup(
        @PathVariable cardGroupId: String,
        @RequestBody request: RemoveCardToGroupRequest
    ): ResponseData<CardListResponse> = cardGroupCardService.deleteCardToGroup(cardGroupId, request, getUserId())

}