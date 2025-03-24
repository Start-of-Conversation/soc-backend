package toyproject.startofconversation.common.domain.user.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import toyproject.startofconversation.common.domain.user.entity.Users
import java.util.*

@Repository
interface UsersRepository : JpaRepository<Users, String> {

    fun existsByEmail(email: String): Boolean

    fun findByEmail(email: String): Optional<Users?>

}