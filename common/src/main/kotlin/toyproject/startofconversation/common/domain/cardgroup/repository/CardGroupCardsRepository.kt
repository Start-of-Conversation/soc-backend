package toyproject.startofconversation.common.domain.cardgroup.repository

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.stereotype.Repository
import toyproject.startofconversation.common.domain.card.entity.Card
import toyproject.startofconversation.common.domain.cardgroup.entity.CardGroup
import toyproject.startofconversation.common.domain.cardgroup.entity.CardGroupCards

@Repository
interface CardGroupCardsRepository : JpaRepository<CardGroupCards, String> {

    fun findAllByCardGroup(pageable: Pageable, cardGroup: CardGroup): Page<CardGroupCards>

    @EntityGraph(attributePaths = ["card"])
    fun findAllByCardGroup(cardGroup: CardGroup): List<CardGroupCards>

    fun countByCardGroup(cardGroup: CardGroup): Int

    @Modifying(flushAutomatically = true, clearAutomatically = false)
    fun deleteAllByCardGroupAndCardIn(cardGroup: CardGroup, cards: List<Card>)

}