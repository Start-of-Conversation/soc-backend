package toyproject.startofconversation.common.domain.user.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import toyproject.startofconversation.common.domain.user.entity.Users

@Repository
interface UsersRepository : JpaRepository<Users, String> {

    @Query("select u.nickname from Users u where u.id = :id")
    fun findNicknameById(@Param("id") id: String): String

}