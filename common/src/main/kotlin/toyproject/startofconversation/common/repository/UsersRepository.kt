package toyproject.startofconversation.common.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import toyproject.startofconversation.common.domain.user.Users

@Repository
interface UsersRepository : JpaRepository<Users, String> {
}