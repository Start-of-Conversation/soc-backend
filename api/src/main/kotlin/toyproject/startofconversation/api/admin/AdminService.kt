package toyproject.startofconversation.api.admin

import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import toyproject.startofconversation.api.admin.dto.AdminUserListResponse
import toyproject.startofconversation.api.admin.repository.AdminUserRepository
import toyproject.startofconversation.api.annotation.AdminUserAccess
import toyproject.startofconversation.api.paging.PageResponseData
import toyproject.startofconversation.api.user.service.UserService
import toyproject.startofconversation.auth.support.AuthValidator
import toyproject.startofconversation.common.base.dto.ResponseData

@Service
@AdminUserAccess
class AdminService(
    private val adminUserRepository: AdminUserRepository,
    private val userService: UserService,
    private val authValidator: AuthValidator
) {

    @Transactional(readOnly = true)
    fun getAllUser(
        pageable: Pageable,
        status: Boolean?,
        userId: String
    ): PageResponseData<List<AdminUserListResponse>> {
        authValidator.validateApprovedAdmin(userId)

        val findAllUsers = adminUserRepository.findAllUsers(pageable, status)
        return PageResponseData(findAllUsers.toList(), findAllUsers)
    }

    @Transactional
    fun approveUser(approvalUserId: String, userId: String): ResponseData<Boolean> {
        authValidator.validateApprovedAdmin(userId)

        return ResponseData(
            "User $approvalUserId approved successfully",
            userService.findUserById(approvalUserId).approve().isApproved
        )
    }

}