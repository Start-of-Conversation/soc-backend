package toyproject.startofconversation.api.admin

import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import toyproject.startofconversation.api.admin.dto.AdminUserListResponse
import toyproject.startofconversation.api.admin.repository.AdminUserRepository
import toyproject.startofconversation.api.paging.PageResponseData

@Service
class AdminService(
    private val adminUserRepository: AdminUserRepository
) {

    fun getAllUser(pageable: Pageable, status: Boolean?): PageResponseData<List<AdminUserListResponse>> {
        val findAllUsers = adminUserRepository.findAllUsers(pageable, status)
        return PageResponseData(findAllUsers.toList(), findAllUsers)
    }

}