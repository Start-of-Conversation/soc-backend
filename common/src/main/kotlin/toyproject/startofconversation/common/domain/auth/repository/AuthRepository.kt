package toyproject.startofconversation.common.domain.auth.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import toyproject.startofconversation.common.domain.auth.entity.Auth

@Repository
interface AuthRepository : JpaRepository<Auth, String> {
}