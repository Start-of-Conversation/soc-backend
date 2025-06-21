package toyproject.startofconversation.api.like

import io.swagger.v3.oas.annotations.Operation
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import toyproject.startofconversation.api.common.BaseController
import toyproject.startofconversation.common.base.dto.ResponseData

@RestController
@RequestMapping("/api")
class LikeController(
    private val likeService: LikeService
) : BaseController() {

    @Operation(summary = "카드그룹 좋아요")
    @PostMapping("/{cardGroupId}/like")
    fun likeCardGroup(
        @PathVariable cardGroupId: String
    ): ResponseData<Boolean> = likeService.like(cardGroupId, getUserId())

    @Operation(summary = "카드그룹 좋아요 취소")
    @DeleteMapping("/{cardGroupId}/like")
    fun unlikeCardGroup(
        @PathVariable cardGroupId: String
    ): ResponseData<Boolean> = likeService.unlike(cardGroupId, getUserId())

}