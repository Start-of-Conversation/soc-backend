package toyproject.startofconversation.api.service

import org.springframework.stereotype.Service
import toyproject.startofconversation.api.dto.RegisterUserRequest
import toyproject.startofconversation.common.repository.UsersRepository

@Service
class UsersService(
    private val usersRepository: UsersRepository
) {

    fun saveUserLocal(request: RegisterUserRequest) {
        val user = request.toUser()
        usersRepository.save(user)

    }

}