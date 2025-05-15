package toyproject.startofconversation.common.base.value

import java.util.*

enum class Domain {
    USER, AUTH, DEVICE, CARD_GROUP, CARD, LIKES, MARKETING;

    override fun toString(): String {
        return this.name.lowercase(Locale.getDefault())
    }
}