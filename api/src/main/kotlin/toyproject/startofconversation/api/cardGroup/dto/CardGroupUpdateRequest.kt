package toyproject.startofconversation.api.cardGroup.dto

data class CardGroupUpdateRequest(
    val name: String? = null,
    val summary: String? = null,
    val description: String? = null,
    val thumbnail: String? = null,
    val isCustomized: Boolean? = null
)
