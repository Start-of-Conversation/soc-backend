package toyproject.startofconversation.auth.local.service

import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import toyproject.startofconversation.auth.controller.dto.LocalLoginRequest
import toyproject.startofconversation.auth.domain.entity.Auth
import toyproject.startofconversation.auth.domain.repository.AuthRepository
import toyproject.startofconversation.auth.exception.InvalidPasswordException
import toyproject.startofconversation.common.domain.user.exception.UserNotFoundException

@Service
class LocalAuthService(
    private val authRepository: AuthRepository,
    private val passwordEncoder: PasswordEncoder
) {

    fun findUser(request: LocalLoginRequest): Auth {
        val auth = authRepository.findByEmail(request.email) ?: throw UserNotFoundException(request.email)

        if (!passwordEncoder.matches(request.password, auth.password)) {
            throw InvalidPasswordException(request.email)
        }

        return auth
    }
}