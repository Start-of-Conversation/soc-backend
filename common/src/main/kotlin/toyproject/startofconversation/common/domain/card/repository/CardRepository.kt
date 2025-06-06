package toyproject.startofconversation.common.domain.card.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import toyproject.startofconversation.common.domain.card.entity.Card

@Repository
interface CardRepository : JpaRepository<Card, String> {

    fun existsByNormalizedQuestion(normalizedQuestion: String): Boolean

}