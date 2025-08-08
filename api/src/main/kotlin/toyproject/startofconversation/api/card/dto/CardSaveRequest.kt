package toyproject.startofconversation.api.card.dto

import jakarta.validation.constraints.NotBlank

data class CardSaveRequest(@field:NotBlank val question: String)
