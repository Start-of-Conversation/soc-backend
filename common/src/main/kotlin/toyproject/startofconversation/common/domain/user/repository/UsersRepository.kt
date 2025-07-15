package toyproject.startofconversation.common.domain.user.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import toyproject.startofconversation.common.domain.user.entity.Users

@Repository
interface UsersRepository : JpaRepository<Users, String> {

}