package toyproject.startofconversation.auth.domain.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import toyproject.startofconversation.auth.domain.entity.Auth
import toyproject.startofconversation.auth.domain.entity.value.AuthProvider

@Repository
interface AuthRepository : JpaRepository<Auth, String> {

    @Query("select a from Auth a join fetch a.user where a.email = :email")
    fun findByEmail(@Param("email") email: String): Auth?

    @Query("select a from Auth a join fetch a.user where a.authProvider = :provider and a.authId = :authId")
    fun findByAuthProviderAndAuthId(
        @Param("provider") provider: AuthProvider,
        @Param("authId") authId: String
    ): Auth?

    fun findByAuthProviderAndUserId(provider: AuthProvider, userId: String): Auth?

    fun deleteAllByUserId(userId: String)

    fun existsByEmail(email: String): Boolean

    fun existsByUserId(userId: String): Boolean

}