package toyproject.startofconversation.common.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import toyproject.startofconversation.common.domain.user.Users
import java.util.*

@Repository
interface UsersRepository : JpaRepository<Users, String> {

    fun existsByEmail(email: String): Boolean

    fun findByEmail(email: String): Optional<Users?>

}