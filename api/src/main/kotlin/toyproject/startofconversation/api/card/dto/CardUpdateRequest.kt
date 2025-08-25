package toyproject.startofconversation.api.card.dto

import jakarta.validation.constraints.NotBlank

class CardUpdateRequest(@field:NotBlank val newQuestion: String)