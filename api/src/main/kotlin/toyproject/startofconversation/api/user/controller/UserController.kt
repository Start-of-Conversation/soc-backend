package toyproject.startofconversation.api.user.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import toyproject.startofconversation.api.user.service.UserService
import toyproject.startofconversation.api.user.dto.UserDataResponse
import toyproject.startofconversation.auth.util.SecurityUtil
import toyproject.startofconversation.common.base.dto.ResponseData

@Tag(name = "User")
@RestController
@RequestMapping("/api/user")
@SecurityRequirement(name = "bearerAuth")
class UserController(
    private val usersService: UserService
) {

    @Operation(
        summary = "내 정보 조회",
        responses = [
            ApiResponse(responseCode = "200", description = "조회 성공"),
            ApiResponse(responseCode = "401", description = "인증 실패"),
            ApiResponse(responseCode = "404", description = "유저를 찾을 수 없음")
        ]
    )
    @GetMapping("/mypage")
    fun getUserInfo(): ResponseData<UserDataResponse> = usersService.searchUserById(SecurityUtil.getCurrentUserId())

    @Operation(
        summary = "회원 탈퇴",
        responses = [
            ApiResponse(responseCode = "200", description = "탈퇴 성공"),
            ApiResponse(responseCode = "401", description = "인증 실패"),
            ApiResponse(responseCode = "404", description = "유저를 찾을 수 없음")
        ]
    )
    @DeleteMapping("/withdrawal")
    fun withdrawalUser(): ResponseData<Boolean> = usersService.deleteUser(SecurityUtil.getCurrentUserId())
}