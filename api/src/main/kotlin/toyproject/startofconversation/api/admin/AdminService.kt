package toyproject.startofconversation.api.admin

import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import toyproject.startofconversation.api.admin.dto.AdminUserListResponse
import toyproject.startofconversation.api.admin.repository.AdminUserRepository
import toyproject.startofconversation.api.paging.PageResponseData
import toyproject.startofconversation.api.user.service.UserService
import toyproject.startofconversation.common.base.dto.ResponseData

@Service
class AdminService(
    private val adminUserRepository: AdminUserRepository,
    private val userService: UserService,
) {

    @Transactional(readOnly = true)
    fun getAllUser(pageable: Pageable, status: Boolean?): PageResponseData<List<AdminUserListResponse>> {
        val findAllUsers = adminUserRepository.findAllUsers(pageable, status)
        return PageResponseData(findAllUsers.toList(), findAllUsers)
    }

    @Transactional
    fun approveUser(userId: String): ResponseData<Boolean> = ResponseData(
        "User $userId approved successfully",
        userService.findUserById(userId).approve().isApproved
    )

}