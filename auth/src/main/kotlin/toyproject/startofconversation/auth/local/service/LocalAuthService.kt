package toyproject.startofconversation.auth.local.service

import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import toyproject.startofconversation.auth.domain.repository.AuthRepository

@Service
class LocalAuthService(
    private val userAuthRepository: AuthRepository,
    private val passwordEncoder: PasswordEncoder
) {
}