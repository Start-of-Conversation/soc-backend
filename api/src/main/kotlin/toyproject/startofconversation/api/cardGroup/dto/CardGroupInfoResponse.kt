package toyproject.startofconversation.api.cardGroup.dto

import toyproject.startofconversation.common.domain.cardgroup.entity.CardGroup

data class CardGroupInfoResponse(
    val id: String,
    val name: String,
    val summary: String,
    val description: String,
    val total: Int,
    val createdBy: String,
    val user: UserInfo
) {
    companion object {

        fun from(
            item: Pair<CardGroup, Long>, userInfo: UserInfo = UserInfo.empty()
        ): CardGroupInfoResponse = with(item.first) {
            CardGroupInfoResponse(
                id = id,
                name = cardGroupName,
                summary = cardGroupSummary,
                description = cardGroupDescription,
                total = item.second.toInt(),
                createdBy = user.id,
                user = userInfo
            )
        }
    }

    data class CreatorInfo(
        val id: String,
        val name: String
    )

    data class UserInfo(
        val id: String?,
        val liked: Boolean
    ) {
        companion object {
            fun empty(): UserInfo = UserInfo(null, false)
        }
    }
}