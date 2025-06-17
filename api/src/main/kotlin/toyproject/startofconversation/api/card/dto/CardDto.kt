package toyproject.startofconversation.api.card.dto

import toyproject.startofconversation.common.domain.card.entity.Card

data class CardDto(val cardId: String, val question: String) {
    companion object {
        fun from(card: Card): CardDto = CardDto(card.id, card.question)
    }
}