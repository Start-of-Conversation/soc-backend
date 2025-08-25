package toyproject.startofconversation.api.cardGroup.dto

import jakarta.validation.constraints.NotBlank

data class CardGroupCreateRequest(
    @field:NotBlank val name: String,
    @field:NotBlank val summary: String,
    @field:NotBlank val description: String,
    val thumbnail: String?,
    val isCustomized: Boolean
)