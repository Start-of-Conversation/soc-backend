package toyproject.startofconversation.api.like

import io.swagger.v3.oas.annotations.Operation
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.web.PageableDefault
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import toyproject.startofconversation.api.cardGroup.dto.CardGroupInfoResponse
import toyproject.startofconversation.api.paging.PageResponseData
import toyproject.startofconversation.common.base.controller.BaseController
import toyproject.startofconversation.common.base.dto.ResponseData

@RestController
@RequestMapping("/api")
class LikeController(
    private val likeService: LikeService
) : BaseController() {

    @Operation(summary = "카드그룹 좋아요")
    @PostMapping("/card-group/{cardGroupId}/like")
    fun likeCardGroup(
        @PathVariable cardGroupId: String
    ): ResponseData<Boolean> = likeService.like(cardGroupId, getUserId())

    @Operation(summary = "카드그룹 좋아요 취소")
    @DeleteMapping("/card-group/{cardGroupId}/like")
    fun unlikeCardGroup(
        @PathVariable cardGroupId: String
    ): ResponseData<Boolean> = likeService.unlike(cardGroupId, getUserId())

    @Operation(summary = "사용자가 좋아요한 카드그룹 조회")
    @GetMapping("/user/likes")
    fun findLikedCardGroupsByUser(
        @PageableDefault(size = 20, page = 0, sort = ["createdAt"], direction = Sort.Direction.DESC) pageable: Pageable
    ): PageResponseData<List<CardGroupInfoResponse>> = likeService.findCardGroupsByUser(getUserId(), pageable)

}