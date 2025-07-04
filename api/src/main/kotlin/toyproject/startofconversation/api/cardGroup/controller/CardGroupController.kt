package toyproject.startofconversation.api.cardGroup.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.web.PageableDefault
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import toyproject.startofconversation.api.cardGroup.dto.CardGroupCreateRequest
import toyproject.startofconversation.api.cardGroup.dto.CardGroupInfoResponse
import toyproject.startofconversation.api.cardGroup.dto.CardGroupUpdateRequest
import toyproject.startofconversation.api.cardGroup.service.CardGroupService
import toyproject.startofconversation.common.base.controller.BaseController
import toyproject.startofconversation.common.base.dto.ResponseData

@Tag(name = "CardGroup")
@RestController
@RequestMapping("/api/card-group")
class CardGroupController(
    private val cardGroupService: CardGroupService
) : BaseController() {

    @GetMapping("/public")
    fun publicCardGroupAll(
        @PageableDefault(size = 20, page = 0, sort = ["createdAt"], direction = Sort.Direction.DESC) pageable: Pageable
    ): ResponseData<List<CardGroupInfoResponse>> = cardGroupService.getCardGroups(pageable)

    @Operation(summary = "카드그룹 정보 조회")
    @GetMapping("/public/{cardGroupId}")
    fun publicCardGroupInfo(@PathVariable cardGroupId: String): ResponseData<CardGroupInfoResponse> =
        cardGroupService.getCardGroupInfo(cardGroupId)

    @Operation(summary = "카드그룹 수정")
    @PatchMapping("/{cardGroupId}")
    fun updateCardGroupInfo(
        @PathVariable cardGroupId: String,
        @RequestBody request: CardGroupUpdateRequest
    ): ResponseData<CardGroupInfoResponse> = cardGroupService.update(cardGroupId, request, getUserId())

    @PostMapping("/add")
    fun createCardGroup(@RequestBody request: CardGroupCreateRequest): ResponseData<CardGroupInfoResponse> =
        cardGroupService.create(request, getUserId())

    @DeleteMapping("/{cardGroupId}")
    fun deleteCardGroup(@PathVariable("cardGroupId") cardGroupId: String): ResponseData<Boolean> =
        cardGroupService.delete(cardGroupId, getUserId())
}