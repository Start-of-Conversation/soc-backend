package toyproject.startofconversation.api.service

import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import toyproject.startofconversation.api.dto.RegisterUserRequest
import toyproject.startofconversation.api.dto.UserDataResponse
import toyproject.startofconversation.api.dto.UserPasswordCheckRequest
import toyproject.startofconversation.common.base.ResponseData
import toyproject.startofconversation.common.exception.SOCException
import toyproject.startofconversation.common.exception.SOCNotSavedException
import toyproject.startofconversation.common.repository.UsersRepository

@Service
class UsersService(
    private val usersRepository: UsersRepository
) {

    fun saveUserLocal(request: RegisterUserRequest): UserDataResponse =
        if (!usersRepository.existsByEmail(request.email)) {
            val user = request.toUser()
            usersRepository.save(user)
            val result = usersRepository.findByIdOrNull(user.getId())
                ?: throw SOCNotSavedException("${request.email} 저장에 싪패하였습니다.")

            UserDataResponse.to(result)
        } else throw SOCException("이미 존재하는 이메일입니다.")

    fun getUserData(id: String): UserDataResponse = if (usersRepository.existsById(id)) {
        val result = usersRepository.findByIdOrNull(id)
            ?: throw SOCException("존재하지 않는 아이디입니다: $id")

        UserDataResponse.to(result)
    } else throw SOCException("")

    fun checkPassword(request: UserPasswordCheckRequest): ResponseData<Boolean> {
        val user = usersRepository.findByIdOrNull(request.id)
            ?: throw SOCException("존재하지 않는 아이디입니다: ${request.id}")

        val encodePassword = BCryptPasswordEncoder().encode(request.password)

        if (user.password == encodePassword) {
            return ResponseData("비밀번호가 일치합니다.", true)
        }

        return ResponseData("비밀번호가 일치하지 않습니다.", false)
    }
}