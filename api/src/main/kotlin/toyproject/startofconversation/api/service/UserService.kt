package toyproject.startofconversation.api.service

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import toyproject.startofconversation.api.dto.UserDataResponse
import toyproject.startofconversation.auth.service.AuthService
import toyproject.startofconversation.common.base.dto.ResponseData
import toyproject.startofconversation.common.domain.user.repository.UsersRepository
import toyproject.startofconversation.common.exception.SOCException
import toyproject.startofconversation.common.exception.SOCNotFoundException
import java.time.LocalDateTime

@Service
class UserService(
    private val usersRepository: UsersRepository,
    private val authService: AuthService,
) {

    fun deleteUser(id: String): ResponseData<Boolean> {
        val user = usersRepository.findByIdOrNull(id)
            ?: throw SOCException("존재하지 않는 아이디입니다: $id")

        if (!user.isDeleted) {
            user.isDeleted = true
            user.deletedAt = LocalDateTime.now()
            usersRepository.save(user)
        }

        authService.deleteAuth(id)

        return ResponseData.to("success", true)
    }

    fun findUserById(id: String): ResponseData<UserDataResponse> {
        val user = usersRepository.findByIdOrNull(id)
            ?: throw SOCNotFoundException("$id is not found")

        return ResponseData.to(UserDataResponse.to(user))
    }

}