package toyproject.startofconversation.common.domain.common.value

import java.util.*

enum class Domain {
    USER;

    override fun toString(): String {
        return this.name.lowercase(Locale.getDefault())
    }
}