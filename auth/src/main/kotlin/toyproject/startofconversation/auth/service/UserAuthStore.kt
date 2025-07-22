package toyproject.startofconversation.auth.service

import org.springframework.stereotype.Service
import toyproject.startofconversation.auth.domain.entity.Auth
import toyproject.startofconversation.auth.domain.entity.value.AuthProvider
import toyproject.startofconversation.auth.domain.repository.AuthRepository
import toyproject.startofconversation.common.domain.user.entity.Users
import toyproject.startofconversation.common.domain.user.repository.UsersRepository
import toyproject.startofconversation.common.transaction.helper.Tx

@Service
class UserAuthStore(
    private val usersRepository: UsersRepository,
    private val authRepository: AuthRepository
) {
    fun getOrCreateUser(email: String, name: String): Users = Tx.writeTx {
        authRepository.findByEmail(email)?.user
            ?: usersRepository.save(Users(nickname = name))
    }

    fun saveAuth(user: Users, email: String, authProvider: AuthProvider, authId: String): Auth = Tx.writeTx {
        authRepository.save(
            Auth(user = user, email = email, authProvider = authProvider, authId = authId)
        )
    }

    fun findOrCreateAuth(
        provider: AuthProvider,
        authId: String,
        createAuth: () -> Auth
    ): Pair<Auth, Boolean> = authRepository.findByAuthProviderAndAuthId(provider, authId)
        ?.let { it to false }
        ?: (createAuth() to true)

}