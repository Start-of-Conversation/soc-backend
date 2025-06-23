package toyproject.startofconversation.api.cardGroup.dto

import toyproject.startofconversation.common.domain.cardgroup.entity.CardGroup
import toyproject.startofconversation.common.domain.like.entity.Likes

data class CardGroupInfoResponse(
    val id: String,
    val name: String,
    val summary: String,
    val description: String,
    val total: Int
) {
    companion object {
        fun from(cardGroup: CardGroup): CardGroupInfoResponse = with(cardGroup) {
            CardGroupInfoResponse(
                id = id,
                name = cardGroupName,
                summary = cardGroupSummary,
                description = cardGroupDescription,
                total = cardGroupCards.size
            )
        }

        fun from(like: Likes): CardGroupInfoResponse = from(like.cardGroup)

    }
}