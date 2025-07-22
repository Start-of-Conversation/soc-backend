package toyproject.startofconversation.common.domain.card.repository.query

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import toyproject.startofconversation.common.domain.card.entity.Card
import java.time.LocalDateTime

interface CardQueryRepository {

    fun findFilteredCards(
        cardGroupId: String?,
        from: LocalDateTime?,
        to: LocalDateTime?,
        userId: String?,
        pageable: Pageable
    ): Page<Card>

}