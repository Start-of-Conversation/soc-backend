package toyproject.startofconversation.api.cardGroup.dto

data class CardGroupCreateRequest(
    val name: String,
    val summary: String,
    val description: String,
    val thumbnail: String?,
    val isCustomized: Boolean
)