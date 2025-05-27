package toyproject.startofconversation.api.cardGroup.dto

import toyproject.startofconversation.common.domain.cardgroup.entity.CardGroup

data class CardGroupInfoResponse(
    val id: String,
    val name: String,
    val summary: String,
    val description: String,
    val total: Int
) {
    companion object {
        fun from(cardGroup: CardGroup): CardGroupInfoResponse {
            return CardGroupInfoResponse(
                id = cardGroup.getId().toString(),
                name = cardGroup.cardGroupName,
                summary = cardGroup.cardGroupSummary,
                description = cardGroup.cardGroupDescription,
                total = cardGroup.cards.size
            )
        }
    }
}