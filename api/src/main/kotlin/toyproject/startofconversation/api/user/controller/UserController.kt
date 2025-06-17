package toyproject.startofconversation.api.user.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort.Direction.DESC
import org.springframework.data.web.PageableDefault
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import toyproject.startofconversation.api.card.CardService
import toyproject.startofconversation.api.card.dto.CardDto
import toyproject.startofconversation.api.common.BaseController
import toyproject.startofconversation.api.paging.PageResponseData
import toyproject.startofconversation.api.user.dto.UserDataResponse
import toyproject.startofconversation.api.user.service.UserService
import toyproject.startofconversation.common.base.dto.ResponseData

@Tag(name = "User")
@RestController
@RequestMapping("/api/user")
@SecurityRequirement(name = "bearerAuth")
class UserController(
    private val usersService: UserService,
    private val cardService: CardService,
) : BaseController() {

    @Operation(
        summary = "내 정보 조회",
        responses = [
            ApiResponse(responseCode = "200", description = "조회 성공"),
            ApiResponse(responseCode = "401", description = "인증 실패"),
            ApiResponse(responseCode = "404", description = "유저를 찾을 수 없음")
        ]
    )
    @GetMapping("/mypage")
    fun getUserInfo(): ResponseData<UserDataResponse> = usersService.searchUserById(getUserId())

    @Operation(
        summary = "회원 탈퇴",
        responses = [
            ApiResponse(responseCode = "200", description = "탈퇴 성공"),
            ApiResponse(responseCode = "401", description = "인증 실패"),
            ApiResponse(responseCode = "404", description = "유저를 찾을 수 없음")
        ]
    )
    @DeleteMapping("/withdrawal")
    fun withdrawalUser(): ResponseData<Boolean> = usersService.deleteUser(getUserId())


    @GetMapping("/mypage/card")
    fun getMyPageCards(
        @PageableDefault(size = 20, page = 0, sort = ["createdAt"], direction = DESC) pageable: Pageable
    ): PageResponseData<List<CardDto>> = cardService.findCardsByUserId(getUserId(), pageable)

}