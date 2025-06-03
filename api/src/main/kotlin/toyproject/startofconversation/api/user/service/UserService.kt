package toyproject.startofconversation.api.user.service

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
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

    @Transactional
    fun deleteUser(id: String): ResponseData<Boolean> = usersRepository.findByIdOrNull(id)?.let {
        if (!it.isDeleted) {
            it.isDeleted = true
            it.deletedAt = LocalDateTime.now()
        }

        authService.deleteAuth(id)

        ResponseData.to("success", true)
    } ?: throw UserNotFoundException(id)

    fun searchUserById(id: String): ResponseData<UserDataResponse> = usersRepository.findByIdOrNull(id)?.let {
        ResponseData.to(UserDataResponse.to(it))
    } ?: throw UserNotFoundException(id)

    fun findUserById(id: String): Users = usersRepository.findByIdOrNull(id)
        ?: throw UserNotFoundException(id)

}