package toyproject.startofconversation.auth.local.service

import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import toyproject.startofconversation.auth.controller.dto.LocalLoginRequest
import toyproject.startofconversation.auth.controller.dto.LocalRegisterRequest
import toyproject.startofconversation.auth.controller.dto.PasswordUpdateRequest
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

    fun findUser(request: LocalLoginRequest): Pair<Auth, Boolean> = Tx.readTx {
        val auth = authRepository.findByEmail(request.email) ?: throw UserNotFoundException(request.email)

        checkPasswordOrThrow(request.password, auth.password, request.email)

        auth to false
    }

    //관리자 전용
    fun saveUser(request: LocalRegisterRequest): ResponseData<Boolean> = Tx.writeTx {
        if (authRepository.existsByEmail(request.email)) {
            throw EmailAlreadyExistsException(request.email)
        }

        val nickname = request.nickname ?: RandomNameMaker.generate()
        val encodedPassword = passwordEncoder.encode(request.password)

        val user = Users(nickname = nickname, role = Role.ADMIN)

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

    @PreAuthorize("isAuthenticated()")
    fun updatePassword(request: PasswordUpdateRequest, userId: String): ResponseData<Boolean> = Tx.writeTx {
        val auth = authRepository.findByAuthProviderAndUserId(AuthProvider.LOCAL, userId)
            ?: throw UserNotFoundException(userId)

        checkPasswordOrThrow(request.oldPassword, auth.password, userId)

        auth.updatePassword(newPassword = passwordEncoder.encode(request.newPassword))

        ResponseData("Successfully updated, User Id: $userId", true)
    }


    private fun isPasswordMismatch(rawPassword: String, encodedPassword: String?): Boolean =
        !passwordEncoder.matches(rawPassword, encodedPassword)

    private fun checkPasswordOrThrow(
        rawPassword: String, encodedPassword: String?, identifier: String
    ) {
        if (isPasswordMismatch(rawPassword, encodedPassword)) {
            throw InvalidPasswordException(identifier)
        }
    }
}