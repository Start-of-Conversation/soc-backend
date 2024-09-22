package toyproject.startofconversation.api.service

import org.springframework.stereotype.Service
import toyproject.startofconversation.common.repository.UsersRepository

@Service
class UsersService(
    private val usersRepository: UsersRepository
) {
}