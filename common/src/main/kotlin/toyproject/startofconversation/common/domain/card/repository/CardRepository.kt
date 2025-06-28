package toyproject.startofconversation.common.domain.card.repository

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import toyproject.startofconversation.common.domain.card.entity.Card
import toyproject.startofconversation.common.domain.card.repository.query.CardQueryRepository

@Repository
interface CardRepository : JpaRepository<Card, String>, CardQueryRepository {

    fun findByIdAndUserId(id: String, userId: String): Card?

    fun findByUserId(userId: String, pageable: Pageable): Page<Card>

    fun findAllByIdIn(ids: List<String>): List<Card>

    fun existsByNormalizedQuestion(normalizedQuestion: String): Boolean

}