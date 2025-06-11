package toyproject.startofconversation.api.admin

import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort.Direction.DESC
import org.springframework.data.web.PageableDefault
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import toyproject.startofconversation.api.admin.dto.AdminUserListResponse
import toyproject.startofconversation.api.paging.PageResponseData

@Tag(name = "Admin")
@RestController
@RequestMapping("/api/admin")
class AdminController(
    private val adminService: AdminService
) {

    @GetMapping("/users")
    fun searchUsers(
        @RequestParam("is-deleted", required = false) isDeleted: Boolean?,
        @PageableDefault(size = 20, page = 0, sort = ["createdAt"], direction = DESC) pageable: Pageable
    ) : PageResponseData<List<AdminUserListResponse>> = adminService.getAllUser(pageable, isDeleted)

}