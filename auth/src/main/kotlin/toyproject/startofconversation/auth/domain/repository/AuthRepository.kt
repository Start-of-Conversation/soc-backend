package toyproject.startofconversation.auth.domain.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import toyproject.startofconversation.auth.domain.entity.Auth
import toyproject.startofconversation.auth.domain.entity.value.AuthProvider

@Repository
interface AuthRepository : JpaRepository<Auth, String> {

    fun findByEmail(email: String): Auth?

    fun findByAuthProviderAndAuthId(provider: AuthProvider, authId: String): Auth?

    fun findByAuthProviderAndUserId(provider: AuthProvider, userId: String): Auth?

    fun deleteAllByUserId(userId: String)

    fun existsByEmail(email: String): Boolean

    fun existsByUserId(userId: String): Boolean

}