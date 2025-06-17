package toyproject.startofconversation.api.admin

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort.Direction.DESC
import org.springframework.data.web.PageableDefault
import org.springframework.web.bind.annotation.*
import toyproject.startofconversation.api.admin.dto.AdminUserListResponse
import toyproject.startofconversation.api.common.BaseController
import toyproject.startofconversation.api.paging.PageResponseData
import toyproject.startofconversation.common.base.dto.ResponseData

@Tag(name = "Admin")
@RestController
@RequestMapping("/api/admin")
class AdminController(
    private val adminService: AdminService
) : BaseController() {

    @Operation(summary = "전체 회원조회")
    @GetMapping("/users")
    fun searchUsers(
        @RequestParam("is-deleted", required = false) isDeleted: Boolean?,
        @PageableDefault(size = 20, page = 0, sort = ["createdAt"], direction = DESC) pageable: Pageable
    ): PageResponseData<List<AdminUserListResponse>> =
        adminService.getAllUser(pageable, isDeleted, getUserId())

    @Operation(summary = "관리자 요청 승인", description = "관리자로 회원가입한 계정을 승인하는 api 입니다.")
    @PatchMapping("/{userId}/approve")
    fun approveUser(
        @PathVariable userId: String
    ): ResponseData<Boolean> = adminService.approveUser(userId, getUserId())

}