package toyproject.startofconversation.common.domain.card.repository

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import toyproject.startofconversation.common.domain.card.entity.Card
import toyproject.startofconversation.common.domain.cardgroup.entity.CardGroup

@Repository
interface CardRepository : JpaRepository<Card, String> {

    fun findAllByCardGroupId(pageable: Pageable, id: String): Page<Card>

    fun findAllByCardGroup(cardGroup: CardGroup): List<Card>

    fun countByCardGroup(cardGroup: CardGroup): Long

}