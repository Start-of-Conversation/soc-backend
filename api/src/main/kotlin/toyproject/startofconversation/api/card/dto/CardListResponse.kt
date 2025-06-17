package toyproject.startofconversation.api.card.dto

import org.springframework.data.domain.Page
import toyproject.startofconversation.common.domain.card.entity.Card

data class CardListResponse(val cardGroupId: String, val cards: List<CardDto>) {
    companion object {
        fun from(cardGroupId: String, cards: Page<Card>): CardListResponse = CardListResponse(
            cardGroupId, cards.map(CardDto::from).toList()
        )

        fun from(cardGroupId: String, cards: List<Card>): CardListResponse = CardListResponse(
            cardGroupId, cards.map(CardDto::from)
        )
    }
}