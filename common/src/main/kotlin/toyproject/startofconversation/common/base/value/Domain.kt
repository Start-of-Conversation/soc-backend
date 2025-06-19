package toyproject.startofconversation.common.base.value

import java.util.Locale

enum class Domain {
    USER, AUTH, DEVICE, CARD_GROUP, CARD, LIKES, MARKETING, CARDGROUP_CARDS;

    override fun toString(): String {
        return this.name.lowercase(Locale.getDefault())
    }
}