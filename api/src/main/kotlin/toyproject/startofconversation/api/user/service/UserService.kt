package toyproject.startofconversation.api.user.service

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import toyproject.startofconversation.api.user.dto.UserDataResponse
import toyproject.startofconversation.auth.service.AuthService
import toyproject.startofconversation.common.base.dto.ResponseData
import toyproject.startofconversation.common.domain.user.entity.Users
import toyproject.startofconversation.common.domain.user.exception.UserNotFoundException
import toyproject.startofconversation.common.domain.user.repository.UsersRepository
import java.time.LocalDateTime

@Service
class UserService(
    private val usersRepository: UsersRepository,
    private val authService: AuthService,
) {

    fun deleteUser(id: String): ResponseData<Boolean> {
        val user = usersRepository.findByIdOrNull(id) ?: throw UserNotFoundException(id)

        if (!user.isDeleted) {
            user.isDeleted = true
            user.deletedAt = LocalDateTime.now()
            usersRepository.save(user)
        }

        authService.deleteAuth(id)

        return ResponseData.Companion.to("success", true)
    }

    fun searchUserById(id: String): ResponseData<UserDataResponse> {
        val user = usersRepository.findByIdOrNull(id) ?: throw UserNotFoundException(id)
        return ResponseData.Companion.to(UserDataResponse.Companion.to(user))
    }

    fun findUserById(id: String): Users = usersRepository.findByIdOrNull(id)
        ?: throw UserNotFoundException(id)

}