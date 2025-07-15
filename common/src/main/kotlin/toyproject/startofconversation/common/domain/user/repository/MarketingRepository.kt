package toyproject.startofconversation.common.domain.user.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import toyproject.startofconversation.common.domain.user.entity.Marketing
import toyproject.startofconversation.common.domain.user.entity.Users

@Repository
interface MarketingRepository : JpaRepository<Marketing, String> {
    fun findByUserId(userId: String): Marketing?

    fun findByUser(user: Users): Marketing?

    fun existsByUserId(userId: String): Boolean
}