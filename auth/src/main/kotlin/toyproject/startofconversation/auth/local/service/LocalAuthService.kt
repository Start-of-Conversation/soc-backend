package toyproject.startofconversation.auth.local.service

import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import toyproject.startofconversation.auth.controller.dto.LocalLoginRequest
import toyproject.startofconversation.auth.controller.dto.LocalRegisterRequest
import toyproject.startofconversation.auth.domain.entity.Auth
import toyproject.startofconversation.auth.domain.entity.value.AuthProvider
import toyproject.startofconversation.auth.domain.repository.AuthRepository
import toyproject.startofconversation.auth.exception.InvalidPasswordException
import toyproject.startofconversation.auth.support.RandomNameMaker
import toyproject.startofconversation.common.base.dto.ResponseData
import toyproject.startofconversation.common.domain.user.entity.Users
import toyproject.startofconversation.common.domain.user.entity.value.Role
import toyproject.startofconversation.common.domain.user.exception.EmailAlreadyExistsException
import toyproject.startofconversation.common.domain.user.exception.UserNotFoundException
import toyproject.startofconversation.common.transaction.helper.Tx

@Service
class LocalAuthService(
    private val authRepository: AuthRepository,
    private val passwordEncoder: PasswordEncoder
) {

    fun findUser(request: LocalLoginRequest): Auth = Tx.readTx {
        val auth = authRepository.findByEmail(request.email) ?: throw UserNotFoundException(request.email)

        if (!passwordEncoder.matches(request.password, auth.password)) {
            throw InvalidPasswordException(request.email)
        }

        auth
    }

    //관리자 전용
    fun saveUser(request: LocalRegisterRequest): ResponseData<Boolean> = Tx.writeTx {
        if (authRepository.existsByEmail(request.email)) {
            throw EmailAlreadyExistsException(request.email)
        }

        val nickname = request.nickname ?: RandomNameMaker.generate()
        val encodedPassword = passwordEncoder.encode(request.password)

        val user = Users(nickname = nickname, role = Role.ADMIN).createMarketing()

        authRepository.save(
            Auth(
                request.email,
                encodedPassword,
                user = user,
                authProvider = AuthProvider.LOCAL
            )
        )

        ResponseData("Successfully saved, User Id: ${user.id}", true)
    }
}