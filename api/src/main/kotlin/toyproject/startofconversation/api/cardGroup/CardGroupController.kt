package toyproject.startofconversation.api.cardGroup

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort.Direction.DESC
import org.springframework.data.web.PageableDefault
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import toyproject.startofconversation.api.card.dto.CardListResponse
import toyproject.startofconversation.api.cardGroup.dto.CardGroupCreateRequest
import toyproject.startofconversation.api.cardGroup.dto.CardGroupInfoResponse
import toyproject.startofconversation.api.common.BaseController
import toyproject.startofconversation.api.paging.PageResponseData
import toyproject.startofconversation.common.base.dto.ResponseData

@Tag(name = "CardGroup")
@RestController
@RequestMapping("/api/card-group")
class CardGroupController(
    private val cardGroupService: CardGroupService
) : BaseController() {

    @GetMapping("/public")
    fun publicCardGroupAll(
        @PageableDefault(size = 20, page = 0, sort = ["createdAt"], direction = DESC) pageable: Pageable
    ): ResponseData<List<CardGroupInfoResponse>> = cardGroupService.getCardGroups(pageable)

    @Operation(summary = "카드그룹 정보 조회")
    @GetMapping("/public/{id}")
    fun publicCardGroupInfo(@PathVariable("id") cardGroupId: String): ResponseData<CardGroupInfoResponse> =
        cardGroupService.getCardGroupInfo(cardGroupId)

    @Operation(summary = "카드그룹에 포함된 카드 리스트 조회 (페이징)")
    @GetMapping("/public/{id}/cards")
    fun getCardsByCardGroup(
        @PathVariable("id") cardGroupId: String,
        @PageableDefault(size = 20, page = 0, sort = ["createdAt"], direction = DESC) pageable: Pageable
    ): PageResponseData<CardListResponse> = cardGroupService.getCards(cardGroupId, pageable)

    @Operation(summary = "카드그룹 좋아요")
    @PostMapping("/{cardGroupId}/like")
    fun likeCardGroup(
        @PathVariable cardGroupId: String
    ): ResponseData<Boolean> = cardGroupService.like(cardGroupId, getUserId())


    @PostMapping("/add")
    fun createCardGroup(@RequestBody request: CardGroupCreateRequest): ResponseData<CardGroupInfoResponse> =
        cardGroupService.create(request, getUserId())

    @DeleteMapping("/{cardGroupId}")
    fun deleteCardGroup(@PathVariable cardGroupId: String): ResponseData<Boolean> =
        cardGroupService.delete(cardGroupId, getUserId())
}