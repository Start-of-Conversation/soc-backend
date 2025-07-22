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
        fun from(item: Pair<CardGroup, Long>): CardGroupInfoResponse = with(item.first) {
            CardGroupInfoResponse(
                id = id,
                name = cardGroupName,
                summary = cardGroupSummary,
                description = cardGroupDescription,
                total = item.second.toInt()
            )
        }
    }
}